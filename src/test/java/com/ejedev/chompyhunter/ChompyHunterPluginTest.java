package com.ejedev.chompyhunter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ChompyHunterPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ChompyHunterPlugin.class);
		RuneLite.main(args);
	}
}