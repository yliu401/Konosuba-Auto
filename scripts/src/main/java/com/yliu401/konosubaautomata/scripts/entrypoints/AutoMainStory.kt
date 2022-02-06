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
    private fun isInHam() = images[Images.Stam] in locations.MainStory.hamRegion
    private fun isInHam2() = images[Images.Stam2] in locations.MainStory.hamRegion2

    private fun isSkip() = images[Images.Skip] in locations.MainStory.skipRegion
    private fun Skip(){
        locations.MainStory.skipClick2.click(13)

    }

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

            if (isZero()) { // zero entries detected so move on //if (isInvalid() || isZero()) {// x0 Has been detected so move left, so tickets cant be used for this stage move left
                //throw ExitException(ExitReason.InventoryFull)
                //click left arrow key
                Duration.seconds(.1).wait()
                if(images[Images.LeftArrow] in locations.MainStory.leftArrowRegion)
                    locations.MainStory.leftArrowClick.click()
                else
                    throw ExitException(ExitReason.InventoryFull)//Should change later on to Left Arrow not found to complete.
            }
            else{
                Duration.seconds(.3).wait()
                if(!done){//checks if x3 has been reset because of out of stamina
                    locations.MainStory.addTicketClick.click(5) //if it still says 0 move left
                    done = true
                }
                //could be an error of no stamina,
                if(!isZero()){//Zero has not been detected, Prepare 3/3 and
                    if(images[Images.Three] !in locations.MainStory.invalidRegion && !refilled){//refill if x3 is not found
                        locations.MainStory.emptyClick.click()
                        while(!isInHam()) {
                            Duration.seconds(.4).wait()
                        }
                        Duration.seconds(.2).wait()
                        while(!isInHam2())
                            locations.MainStory.hamClick.click(3)
                        Duration.seconds(.3).wait()
                        locations.MainStory.okHamClick.click()
                        refilled = true
                        count++

                        Duration.seconds(1.5).wait()
                        locations.MainStory.addTicketClick.click(13)
                        Duration.seconds(.7).wait()

                        done = false
                    }

                    locations.MainStory.useTicketsClick.click()//click use tickets
                    //Duration.seconds(0.1).wait()//maybe add while loop to check if ok is there?
                    locations.clickOk.click(5)//click ok
                    while(!isSkip()){
                        Duration.seconds(0.25).wait()
                    }
                    while(images[Images.useTickets] !in locations.MainStory.invalidRegion) {// tickets is not on screen.
                        locations.MainStory.skipClick.click(15)
                        Duration.seconds(0.25).wait()
                    }

                    refilled = false
                }

                else if(isInvalid()){//if x0 still move left cause stage either has not been three crowned
                    done = false
                    if(images[Images.LeftArrow] in locations.MainStory.leftArrowRegion)
                        locations.MainStory.leftArrowClick.click()
                }

            }

        }

    }
}