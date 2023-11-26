package com.newwind.nwtweaks.sanity;

import croissantnova.sanitydim.passive.IPassiveSanitySource;

import java.util.Arrays;
import java.util.List;

public class SanityCommon {

	public static final List<IPassiveSanitySource> PASSIVE_SANITY_SOURCES = Arrays.asList(new ExtremeTemperature(), new CalmingTemperature(), new StaringAtDweller());

}
