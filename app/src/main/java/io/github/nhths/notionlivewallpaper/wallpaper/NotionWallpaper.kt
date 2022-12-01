package io.github.nhths.notionlivewallpaper.wallpaper

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class NotionWallpaper : WallpaperService() {
    override fun onCreateEngine(): NotionWallpaperEngine {
        return NotionWallpaperEngine()
    }

    inner class NotionWallpaperEngine : WallpaperService.Engine() {
        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)
        }
    }

}