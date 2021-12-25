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
    //Detects region where it is x0
    val invalidRegion =  Region(-120, 1050, 430, 150).xFromCenter()
    val leftArrow = Location(30, 550).xFromCenter()

}