package com.doctoror.fuckoffmusicplayer.db.playlist;

import com.doctoror.fuckoffmusicplayer.queue.Media;

import java.util.List;

/**
 * Factory for creating "recently scanned" playlist
 */
public interface PlaylistProviderRecentlyScanned {

    List<Media> recentlyScannedPlaylist();

}
