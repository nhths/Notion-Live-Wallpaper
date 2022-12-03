package io.github.nhths.notionlivewallpaper.wallpaper

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Rect
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.View
import android.widget.TextView


class NotionWallpaper : WallpaperService() {
    override fun onCreateEngine(): NotionWallpaperEngine {
        return NotionWallpaperEngine()
    }

    inner class NotionWallpaperEngine : WallpaperService.Engine() {
        override fun onCreate(surfaceHolder: SurfaceHolder?) {
            super.onCreate(surfaceHolder)

        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder?) {
            super.onSurfaceRedrawNeeded(holder)
            Thread().run {
                val rect = Rect()
                rect[100, 100, 300] = 300

                //Make a new view and lay it out at the desired Rect dimensions
                val view = TextView(displayContext)
                view.text = "This is a custom drawn textview"
                view.setBackgroundColor(Color.RED)
                view.gravity = Gravity.CENTER

                //Measure the view at the exact dimensions (otherwise the text won't center correctly)

                //Measure the view at the exact dimensions (otherwise the text won't center correctly)
                val widthSpec: Int =
                    View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY)
                val heightSpec: Int =
                    View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)
                view.measure(widthSpec, heightSpec)

                //Lay the view out at the rect width and height

                //Lay the view out at the rect width and height
                view.layout(0, 0, rect.width(), rect.height())

                //Translate the Canvas into position and draw it

                //Translate the Canvas into position and draw it
                val va = ValueAnimator.ofFloat(0f, 700f)
                val mDuration = 20000 //in millis

                va.duration = mDuration.toLong()
                va.addUpdateListener {
                    val canvas = surfaceHolder.lockCanvas()
                    if (canvas != null) {
                        canvas.drawColor(Color.CYAN)
                        canvas.translate(it.animatedValue as Float, rect.top.toFloat())
                        view.draw(canvas)
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
                va.repeatCount = 5
                va.start()
            }

            if (surfaceHolder != null) {
                Log.d("", surfaceHolder.surfaceFrame.width().toString())
            }
        }
    }

}