package ru.inno.market.runner;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"ru.inno.market"})
@IncludeTags("regress")
public class TestRun {
}
