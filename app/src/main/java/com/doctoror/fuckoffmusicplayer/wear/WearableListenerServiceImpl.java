package com.doctoror.fuckoffmusicplayer.wear;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;

import com.doctoror.commons.util.Log;
import com.doctoror.commons.wear.DataPaths;
import com.doctoror.commons.wear.nano.WearQueueFromSearch;
import com.doctoror.fuckoffmusicplayer.db.playlist.PlaylistProviderAlbums;
import com.doctoror.fuckoffmusicplayer.db.playlist.PlaylistProviderArtists;
import com.doctoror.fuckoffmusicplayer.db.playlist.PlaylistProviderTracks;
import com.doctoror.fuckoffmusicplayer.di.DaggerHolder;
import com.doctoror.fuckoffmusicplayer.playback.PlaybackServiceControl;
import com.doctoror.fuckoffmusicplayer.playback.data.PlaybackData;
import com.doctoror.fuckoffmusicplayer.queue.Media;
import com.doctoror.fuckoffmusicplayer.queue.QueueUtils;

import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link WearableListenerService} implementation
 */
public final class WearableListenerServiceImpl extends WearableListenerService {

    private static final String TAG = "WearableListenerServiceImpl";

    @Inject
    PlaylistProviderArtists mArtistPlaylistFactory;

    @Inject
    PlaylistProviderAlbums mAlbumPlaylistFactory;

    @Inject
    PlaylistProviderTracks mTrackPlaylistFactory;

    @Inject
    PlaybackData mPlaybackData;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerHolder.getInstance(this).mainComponent().inject(this);
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        switch (messageEvent.getPath()) {
            case DataPaths.Messages.PLAY_PAUSE:
                PlaybackServiceControl.playPause(this);
                break;

            case DataPaths.Messages.PREV:
                PlaybackServiceControl.prev(this);
                break;

            case DataPaths.Messages.NEXT:
                PlaybackServiceControl.next(this);
                break;

            case DataPaths.Messages.SEEK: {
                final byte[] data = messageEvent.getData();
                if (data != null && data.length == 4) {
                    final float positionPercent = ByteBuffer.wrap(data).getFloat();
                    PlaybackServiceControl.seek(this, positionPercent);
                }
                break;
            }

            case DataPaths.Messages.PLAY_FROM_QUEUE: {
                final byte[] data = messageEvent.getData();
                if (data != null && data.length == 8) {
                    final long mediaId = ByteBuffer.wrap(data).getLong();
                    PlaybackServiceControl.playMediaFromQueue(this, mediaId);
                }
                break;
            }

            case DataPaths.Messages.SEARCH: {
                final byte[] data = messageEvent.getData();
                if (data != null) {
                    final String query = new String(data, Charset.forName("UTF-8"));
                    WearableSearchProviderService.search(this, query);
                }
                break;
            }

            case DataPaths.Messages.PLAY_ALBUM: {
                final byte[] data = messageEvent.getData();
                if (data != null && data.length == 8) {
                    final long id = ByteBuffer.wrap(data).getLong();
                    final List<Media> queue = mAlbumPlaylistFactory.fromAlbum(id);
                    playQueue(queue, 0);
                }
                break;
            }

            case DataPaths.Messages.PLAY_ARTIST: {
                final byte[] data = messageEvent.getData();
                if (data != null && data.length == 8) {
                    final long id = ByteBuffer.wrap(data).getLong();
                    final List<Media> queue = mArtistPlaylistFactory.fromArtist(id);
                    playQueue(queue, 0);
                }
                break;
            }

            case DataPaths.Messages.PLAY_TRACK: {
                final byte[] data = messageEvent.getData();
                if (data != null) {
                    final WearQueueFromSearch.Queue fromSearch;
                    try {
                        fromSearch = WearQueueFromSearch.Queue.parseFrom(data);
                    } catch (InvalidProtocolBufferNanoException e) {
                        Log.w(TAG, e);
                        break;
                    }
                    final List<Media> queue = mTrackPlaylistFactory.fromTracks(
                            fromSearch.queue, MediaStore.Audio.Media.TITLE);
                    if (queue != null && !queue.isEmpty()) {
                        int index = 0;
                        for (int i = 0; i < queue.size(); i++) {
                            final Media media = queue.get(i);
                            if (media.getId() == fromSearch.selectedId) {
                                index = i;
                                break;
                            }
                        }
                        playQueue(queue, index);
                    }
                }
                break;
            }
        }
    }

    private void playQueue(@Nullable final List<Media> queue, final int position) {
        if (queue != null && !queue.isEmpty()) {
            QueueUtils.play(this, mPlaybackData, queue, position);
        }
    }
}
