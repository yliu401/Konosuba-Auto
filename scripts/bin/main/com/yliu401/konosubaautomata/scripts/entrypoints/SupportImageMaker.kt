package com.yliu401.konosubaautomata.scripts.entrypoints

import com.yliu401.konosubaautomata.IStorageProvider
import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.konosubaautomata.scripts.modules.Support
import com.yliu401.libautomata.EntryPoint
import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.IPattern
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import java.io.File
import javax.inject.Inject

@ScriptScope
class SupportImageMaker @Inject constructor(
    storageProvider: IStorageProvider,
    exitManager: ExitManager,
    api: IFgoAutomataApi
) : EntryPoint(exitManager), IFgoAutomataApi by api {
    companion object {
        fun getServantImgPath(dir: File, Index: Int): File {
            return File(dir, "servant_${Index}.png")
        }

        fun getCeImgPath(dir: File, Index: Int): File {
            return File(dir, "ce_${Index}.png")
        }

        fun getFriendImgPath(dir: File, Index: Int): File {
            return File(dir, "friend_${Index}.png")
        }
    }

    sealed class ExitReason {
        object Success: ExitReason()
        object NotFound: ExitReason()
    }

    class ExitException(val reason: ExitReason) : Exception()

    private val dir = storageProvider.supportImageTempDir

    override fun script(): Nothing {
        cleanExtractFolder()

        val isInSupport = isInSupport()

        // At max two Servant+CE are completely on screen, so only use those
        val regionArray = locations.scriptArea
            .findAll(
                images[Images.SupportConfirmSetupButton],
                Support.supportRegionToolSimilarity
            )
            .map {
                Region(
                    if (isInSupport) -2072 else -2064,
                    // in the friend screen, the "Confirm Support Setup" button is higher
                    if (isInSupport) 66 else 82,
                    284,
                    220
                ) + it.region.location
            }
            .filter { it in locations.scriptArea }
            .take(2)
            .toList()
            .sorted()

        for ((i, region) in regionArray.withIndex()) {
            region.getPattern().use {
                extractServantImage(it, i)
                extractCeImage(it, i)
                extractFriendNameImage(region, isInSupport, i)
            }
        }

        if (regionArray.isEmpty()) {
            throw ExitException(ExitReason.NotFound)
        }

        throw ExitException(ExitReason.Success)
    }

    private fun cleanExtractFolder() {
        dir.listFiles()?.forEach {
            it.delete()
        }
    }

    private fun IPattern.save(path: File) = use {
        path.outputStream().use { stream ->
            save(stream)
        }
    }

    private fun extractServantImage(supportBoundImage: IPattern, i: Int) {
        val servant = supportBoundImage.crop(Region(0, 0, 125, 44))
        servant.save(getServantImgPath(dir, i))
    }

    private fun extractCeImage(supportRegionImage: IPattern, i: Int) {
        val ce = supportRegionImage.crop(Region(0, 80, supportRegionImage.width, 25))
        ce.save(getCeImgPath(dir, i))
    }

    private fun extractFriendNameImage(supportBound: Region, isInSupport: Boolean, i: Int) {
        // the friend name is further to the left in the friend screen
        val friendNameX = supportBound.x + (if (isInSupport) 364 else 344)
        val friendBound = Region(friendNameX, supportBound.y - 95, 400, 110)

        val friendPattern = friendBound.getPattern()
        friendPattern.save(getFriendImgPath(dir, i))
    }
}