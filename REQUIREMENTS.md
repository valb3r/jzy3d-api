# Requirements

I guess 1-3 is highly wanted for a first version. 4 and 5 is nice and would be great to have but not a must for a first release.

// --------------------------------

1) you need to support small numbers for tick labels. Labels should display properly. Non displayable labels should not yield to tons of logs and neither to application freeze. The link I sent you should help you fix the problem, but it is probably faster fix it rather than explaining it. I propose to reuse what Eusebio sent some time ago to reproduce the problem.

Yes, high prio item and good if you show a solution using Eusebios test case. Scientific notation is good(e.g. 1e-27  1e27) for small/large numbers.

2) Working solution for os-x. I know that this might be outside your code but we should have a plan for how to proceed.

3)log and log10 axis type  support.

// --------------------------------

4) when plotting multiple surfaces, you want to add a text to identify each surface

Yes, a label that can be turned on/off from the gui panel or a tooltip that only is visible when hoovering the mouse over a surface. Or some other bettwer way to identify multiple surfaces.

5) in case you plot more than 2 surfaces with different units for Z, you need a visualization method to show these 3 units.

Yes, if plotting different quantities e.g. an energy in Joule at the same time as a temperature in Kelvin on need to visualize two z-axis. See example for how we do it with JFreeChart and multiple y-axes for a 2d plot. First with same quantity type and unit(Temperature in Kelvin) This result in same y-axis

If changing unit and plotting three temperatures in different units one get three y-axis that one can set individual axis limits on.