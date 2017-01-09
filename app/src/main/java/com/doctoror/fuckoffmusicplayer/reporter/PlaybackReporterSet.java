package com.doctoror.fuckoffmusicplayer.reporter;

import com.doctoror.commons.playback.PlaybackState;
import com.doctoror.fuckoffmusicplayer.queue.Media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Reports to multiple {@link PlaybackReporter}s
 */
final class PlaybackReporterSet implements PlaybackReporter {

    @NonNull
    private final PlaybackReporter[] mReporters;

    PlaybackReporterSet(@NonNull final PlaybackReporter... reporters) {
        mReporters = reporters;
    }

    @Override
    public void reportTrackChanged(@NonNull final Media media, final int positionInQueue) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < mReporters.length; i++) {
            mReporters[i].reportTrackChanged(media, positionInQueue);
        }
    }

    @Override
    public void reportPlaybackStateChanged(@PlaybackState.State final int state,
            @Nullable final CharSequence errorMessage) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < mReporters.length; i++) {
            mReporters[i].reportPlaybackStateChanged(state, errorMessage);
        }
    }

    @Override
    public void reportPositionChanged(final long mediaId, final long position) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < mReporters.length; i++) {
            mReporters[i].reportPositionChanged(mediaId, position);
        }
    }

    @Override
    public void reportQueueChanged(@Nullable final List<Media> queue) {
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < mReporters.length; i++) {
            mReporters[i].reportQueueChanged(queue);
        }
    }
}
