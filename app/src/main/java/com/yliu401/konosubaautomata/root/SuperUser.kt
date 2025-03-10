package com.yliu401.konosubaautomata.root

import com.yliu401.konosubaautomata.util.KnownException
import dagger.hilt.android.scopes.ServiceScoped
import java.io.DataInputStream
import java.io.DataOutputStream
import javax.inject.Inject

/**
 * This class can execute shell commands with superuser rights.
 */
@ServiceScoped
class SuperUser @Inject constructor() : AutoCloseable {
    private val superUser: Process
    private val outStream: DataOutputStream
    val inStream: DataInputStream

    /**
     * Requests superuser rights and checks if the attempt was successful.
     *
     * @throws [KnownException] if the attempt was unsuccessful
     */
    init {
        try {
            superUser = Runtime.getRuntime().exec("su", null, null)
            outStream = DataOutputStream(superUser.outputStream)
            inStream = DataInputStream(superUser.inputStream)

            // We can check if we got root by sending 'id' to the su process which returns current user's id.
            // The response should contain 'uid=0', where 0 is the id of root user.
            writeLine("id")

            // Don't dispose reader or InputStream will be closed
            val reader = superUser.inputStream.bufferedReader()
            val response = reader.readLine()
            if (!response.contains("uid=0")) {
                throw Exception("Not root user")
            }
        } catch (e: Exception) {
            throw KnownException(KnownException.Reason.FailedRootPermission(e))
        }
    }

    /**
     * Writes a line to the shell.
     */
    fun writeLine(Line: String) {
        outStream.writeBytes("${Line}\n")
        outStream.flush()
    }

    /**
     * Waits until the previous command has finished.
     */
    private fun waitForCommand() {
        // https://stackoverflow.com/a/16160785/5377194
        writeLine("echo -n 0")

        superUser.inputStream.read()
    }

    /**
     * Exits the shell, which also terminates the superuser session.
     */
    override fun close() {
        writeLine("exit")
        superUser.waitFor()
    }
}