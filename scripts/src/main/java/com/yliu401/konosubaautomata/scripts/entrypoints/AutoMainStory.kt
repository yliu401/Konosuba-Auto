package com.yliu401.konosubaautomata.scripts.entrypoints

import com.yliu401.konosubaautomata.scripts.IFgoAutomataApi
import com.yliu401.konosubaautomata.scripts.Images
import com.yliu401.libautomata.EntryPoint
import com.yliu401.libautomata.ExitManager
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration

/**
 * Go from Part1-> Normal to Hard -> Interlude -> Click on top most,
 */
@ScriptScope
class AutoMainStory @Inject constructor(
    exitManager: ExitManager,
    api: IFgoAutomataApi
) : EntryPoint(exitManager), IFgoAutomataApi by api {
    sealed class ExitReason {
        object InventoryFull: ExitReason()
        class Limit(val count: Int): ExitReason()
    }

    class ExitException(val reason: ExitReason): Exception()

    private var count = 0

    private fun countNext() {
        if (prefs.shouldLimitFP && count >= prefs.limitFP) {
            throw ExitException(ExitReason.Limit(count))
        }
        ++count
    }
    //Testing if Invalid Ticket use can be mis-detected
    private fun isInvalid() = images[Images.Inv_Ticket] in locations.MainStory.invalidRegion
    private fun isEmpty() = images[Images.Empty] in locations.MainStory.emptyRegion
    private fun isZero() = images[Images.Zero] in locations.MainStory.zeroRegion


    override fun script(): Nothing {
       /* if (images[Images.FPSummonContinue] !in locations.fp.continueSummonRegion) {
            locations.fp.first10SummonClick.click()
            Duration.seconds(0.3).wait()
            locations.fp.okClick.click()

            countNext()
        }*/
        if(images[Images.MainStoryP1] in locations.MainStory.part1Region){
            locations.MainStory.part1Click.click()
            Duration.seconds(1.2).wait()//Have to wait for Hard to Load,
        }

        if(images[Images.Hard] in locations.MainStory.hardRegion){
            locations.MainStory.hardClick.click()
            Duration.seconds(1.2).wait()
            locations.MainStory.interludeClick.click()
            Duration.seconds(1.2).wait()
            locations.MainStory.startClick.click()
            Duration.seconds(3).wait()
        }
        var refilled: Boolean = false//just so it doesnt get caught in infinite loop.
        var done: Boolean = false


        while (true) {
            Duration.seconds(.3).wait()

            if (isInvalid() || isZero()) {// x0 Has been detected so move left, so tickets cant be used for this stage move left
                //throw ExitException(ExitReason.InventoryFull)
                //click left arrow key


                if(images[Images.LeftArrow] in locations.MainStory.leftArrowRegion)
                    locations.MainStory.leftArrowClick.click()
                else
                    throw ExitException(ExitReason.InventoryFull)//Should change later on to Left Arrow not found to complete.
            }
            else{
                Duration.seconds(.3).wait()
                if(!done){
                    locations.MainStory.addTicketClick.click(4) //if it still says 0 move left
                    done = true
                }

                if(isInvalid()){
                     if(images[Images.LeftArrow] in locations.MainStory.leftArrowRegion)
                        locations.MainStory.leftArrowClick.click()
                }
                else{
                    if(images[Images.Three] !in locations.MainStory.invalidRegion && !refilled){//refill if x3 is not found
                        locations.MainStory.emptyClick.click()
                        Duration.seconds(.3).wait()
                        locations.MainStory.hamClick.click()
                        Duration.seconds(.3).wait()
                        locations.MainStory.okHamClick.click()
                        refilled = true
                        count++
                        Duration.seconds(1).wait()
                        locations.MainStory.addTicketClick.click(5)
                        Duration.seconds(1).wait()
                    }
                    locations.MainStory.useTicketsClick.click()//click use tickets
                    Duration.seconds(0.6).wait()
                    locations.clickOk.click()//click ok
                    Duration.seconds(1).wait()
                    locations.MainStory.skipClick.click(20)
                    refilled = false
                }

            }

        }

    }
}