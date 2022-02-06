package com.yliu401.konosubaautomata.scripts.locations

import com.yliu401.libautomata.Location
import com.yliu401.libautomata.Region
import com.yliu401.libautomata.dagger.ScriptScope
import javax.inject.Inject

@ScriptScope
class MainStoryLocations @Inject constructor(
    scriptAreaTransforms: IScriptAreaTransforms
): IScriptAreaTransforms by scriptAreaTransforms {
    //Part1 Of Main Story C_2
    val part1Region = Region(-120, 430, 260, 90).xFromCenter()
    val part1Click = Location(0, 550).xFromCenter()
    //x3 region



    //Hard Button Region. clicks in this order
    val hardRegion = Region(350, 20, 220, 140).xFromCenter()
    val hardClick = Location(600,100).xFromCenter()
    val interludeClick = Location(400,800).xFromCenter()
    val startClick = Location(600,400).xFromCenter()//highest level Stage X-4 location

    //Detects region where it is x0
    val invalidRegion =  Region(-120, 1050, 430, 150).xFromCenter()
    //clicks + for tickets region
    val addTicketClick = Location( 350,1120).xFromCenter()
    val useTicketsClick = Location(0, 1100).xFromCenter()
    val skipClick = Location(0, 1250).xFromCenter()

    //EMPTY STAMINA
    val emptyRegion = Region(620, 850, 430, 150).xFromCenter()
    val emptyClick = Location(940,1000).xFromCenter()
    val hamRegion = Region(-160, 600, 230, 260).xFromCenter()
    val hamRegion2 = Region(-170, 200, 240, 260).xFromCenter()
    val hamClick = Location(-50,750).xFromCenter()

    val okHamClick = Location(200,1270).xFromCenter()

    val zeroRegion = Region(620, 1050, 430, 150).xFromCenter()


    //clicks left arrow region
    val leftArrowRegion = Region(-540, 640, 200, 150).xFromCenter() //left arrow key
    val leftArrowClick = Location(-450, 675).xFromCenter()


    //C_3 For Skip complete
    val skipRegion = Region(-360, 1150, 600, 220).xFromCenter()
    val skipClick2 = Location(0, 1190).xFromCenter()


}