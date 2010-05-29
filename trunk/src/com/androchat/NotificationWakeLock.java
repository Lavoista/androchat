package com.androchat;

import android.content.Context;
import android.os.PowerManager;

// This is modeled after the way the Android alarm clock application handles
// its WakeLock.
class NotificationWakeLock {
	private static PowerManager.WakeLock wakeLock;

	static void acquire(Context context) {
		PowerManager.WakeLock old = wakeLock;
		wakeLock = ((PowerManager) context.getSystemService(
			Context.POWER_SERVICE)).newWakeLock(
			PowerManager.PARTIAL_WAKE_LOCK, "AndroChat");
		wakeLock.acquire();
		if (old != null) {
			old.release();
		}
	}

	static void release() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
	}
}
