package io.github.nhths.notionlivewallpaper.wallpaper

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi


class NotionWallpaper : WallpaperService() {
    override fun onCreateEngine(): NotionWallpaperEngine {
        return NotionWallpaperEngine()
    }

    inner class NotionWallpaperEngine : WallpaperService.Engine() {
        var lastRender = System.currentTimeMillis();

        val thread = HandlerThread("MyHandlerThread").apply {
            start()
        }

        var handler = Handler(thread.looper)

        val rect = Rect().apply {
            set(200, 500, 400, 700)
        }

        val widthSpec: Int =
            View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY)
        val heightSpec: Int =
            View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)

        lateinit var view: TextView

        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)

            view = TextView(displayContext).apply {
                text = "This is a custom drawn textview"
                setBackgroundColor(Color.RED)
                gravity = Gravity.CENTER
                measure(widthSpec, heightSpec)
                layout(0, 0, rect.width(), rect.height())
            }

        }

        @RequiresApi(Build.VERSION_CODES.R)
        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder?) {
            super.onSurfaceRedrawNeeded(holder)
            handler.postDelayed({redraw()}, 8)
        }

        fun redraw(){
            Log.d("redraw", "")
            val canvas = surfaceHolder.lockCanvas()
            if (canvas != null) {
                canvas.drawColor(Color.CYAN)
                canvas.translate(0f, rect.top.toFloat())
                view.draw(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
            if (System.currentTimeMillis() - 8 > lastRender) {
                handler.post { redraw() };
            } else {
                handler.postDelayed({redraw()}, System.currentTimeMillis() - lastRender)
            }
            lastRender = System.currentTimeMillis()
        }

    }

}