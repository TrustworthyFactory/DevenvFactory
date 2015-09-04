package com.thalesgroup.optet.devenv.compilation;


import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public abstract interface MavenLaunchConstants
{
  public static final String PLUGIN_ID = "org.eclipse.m2e.launching";
  public static final String LAUNCH_CONFIGURATION_TYPE_ID = "org.eclipse.m2e.Maven2LaunchConfigurationType";
  public static final String BUILDER_CONFIGURATION_TYPE_ID = "org.eclipse.m2e.Maven2BuilderConfigurationType";
  public static final String ATTR_POM_DIR = IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY;
  public static final String ATTR_GOALS = "M2_GOALS";
  public static final String ATTR_GOALS_AUTO_BUILD = "M2_GOALS_AUTO_BUILD";
  public static final String ATTR_GOALS_MANUAL_BUILD = "M2_GOALS_MANUAL_BUILD";
  public static final String ATTR_GOALS_CLEAN = "M2_GOALS_CLEAN";
  public static final String ATTR_GOALS_AFTER_CLEAN = "M2_GOALS_AFTER_CLEAN";
  public static final String ATTR_PROFILES = "M2_PROFILES";
  public static final String ATTR_PROPERTIES = "M2_PROPERTIES";
  public static final String ATTR_OFFLINE = "M2_OFFLINE";
  public static final String ATTR_UPDATE_SNAPSHOTS = "M2_UPDATE_SNAPSHOTS";
  public static final String ATTR_DEBUG_OUTPUT = "M2_DEBUG_OUTPUT";
  public static final String ATTR_SKIP_TESTS = "M2_SKIP_TESTS";
  public static final String ATTR_NON_RECURSIVE = "M2_NON_RECURSIVE";
  public static final String ATTR_WORKSPACE_RESOLUTION = "M2_WORKSPACE_RESOLUTION";
  public static final String ATTR_USER_SETTINGS = "M2_USER_SETTINGS";
  public static final String ATTR_RUNTIME = "M2_RUNTIME";
  public static final String ATTR_FORCED_COMPONENTS_LIST = "M2_FORCED_COMPONENTS_LIST";
  public static final String ATTR_DISABLED_EXTENSIONS = "M2_DISABLED_EXTENSIONS";
}