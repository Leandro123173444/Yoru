package com.aurora.music.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

object CrashLog {
    private const val FILE = "last_crash.txt"

    fun install(app: Application) {
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            runCatching {
                val writer = StringWriter()
                error.printStackTrace(PrintWriter(writer))
                File(app.filesDir, FILE).writeText("Hilo: ${thread.name}\nAndroid: ${android.os.Build.VERSION.SDK_INT} (${android.os.Build.MODEL})\n\n$writer")
            }
            previous?.uncaughtException(thread, error)
        }
    }

    private fun shorten(text: String): String {
        val lines = text.lines()
        val keep = LinkedHashSet<Int>()
        for (i in 0 until minOf(14, lines.size)) keep += i
        lines.forEachIndexed { i, line ->
            if (line.startsWith("Caused by")) for (j in i until minOf(i + 7, lines.size)) keep += j
        }
        return keep.sorted().joinToString("\n") { lines[it] }
    }

    fun showIfPending(activity: Activity) {
        val file = File(activity.filesDir, FILE)
        if (!file.exists()) return
        val text = runCatching { shorten(file.readText()) }.getOrNull().orEmpty()
        file.delete()
        if (text.isBlank() || activity.isFinishing) return
        val view = TextView(activity).apply {
            this.text = text
            textSize = 11f
            typeface = Typeface.MONOSPACE
            setTextIsSelectable(true)
            setPadding(40, 20, 40, 20)
        }
        AlertDialog.Builder(activity)
            .setTitle("YORU se cerró la última vez")
            .setView(ScrollView(activity).apply { addView(view) })
            .setPositiveButton("Copiar") { _, _ ->
                val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("YORU error", text))
                Toast.makeText(activity, "Error copiado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }
}
