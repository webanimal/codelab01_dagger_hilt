package com.example.android.hilt.core.contentprovider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.example.android.hilt.data.LogDao
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class LogsContentProvider : ContentProvider() {
	
	private val matcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
		addURI(AUTHORITY, LOGS_TABLE, CODE_LOGS_DIR)
		addURI(AUTHORITY, "$LOGS_TABLE/*", CODE_LOGS_ITEM)
	}
	
	override fun onCreate(): Boolean = true
	
	override fun query(
		uri: Uri,
		projection: Array<out String>?,
		selection: String?,
		selectionArgs: Array<out String>?,
		sortOrder: String?
	): Cursor? {
		return when (val code = matcher.match(uri)) {
			CODE_LOGS_DIR,
			CODE_LOGS_ITEM -> {
				val appContext = context?.applicationContext ?: throw IllegalStateException()
				val logDao: LogDao = getLogDao(appContext)
				val cursor: Cursor? = if (code == CODE_LOGS_DIR) {
					logDao.getAllCursor()
				} else {
					logDao.getByIdCursor(ContentUris.parseId(uri))
				}
				cursor?.setNotificationUri(appContext.contentResolver, uri)
				cursor
			}
			else -> throw IllegalArgumentException("Unknown URI: $uri")
		}
	}
	
	override fun getType(uri: Uri): String? {
		throw ReadOnlySupportedException()
	}
	
	override fun insert(uri: Uri, values: ContentValues?): Uri? {
		throw ReadOnlySupportedException()
	}
	
	override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
		throw ReadOnlySupportedException()
	}
	
	override fun update(
		uri: Uri,
		values: ContentValues?,
		selection: String?,
		selectionArgs: Array<out String>?
	): Int {
		throw ReadOnlySupportedException()
	}
	
	private fun getLogDao(appContext: Context): LogDao = EntryPointAccessors.fromApplication(
		appContext,
		LogsContentProviderEntryPoint::class.java
	).logDao()
	
	@EntryPoint
	@InstallIn(SingletonComponent::class)
	interface LogsContentProviderEntryPoint {
		fun logDao(): LogDao
	}
	
	abstract class LogsContentProviderException(msg: String) : UnsupportedOperationException(msg)
	class ReadOnlySupportedException(
		msg: String = "Only reading operations are allowed"
	) : LogsContentProviderException(msg)
}

private const val LOGS_TABLE = "logs"
/** The authority of this content provider. */
private const val AUTHORITY = "com.example.android.hilt.logs_provider"
/** The match code for some items in the Logs table.  */
private const val CODE_LOGS_DIR = 1
/** The match code for an item in the Logs table.  */
private const val CODE_LOGS_ITEM = 2