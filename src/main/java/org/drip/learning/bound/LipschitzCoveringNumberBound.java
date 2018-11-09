
package org.drip.learning.bound;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, and portfolio construction within and across fixed income, credit, commodity, equity,
 *  	FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three main modules:
 *  
 *  - DROP Analytics Core - https://lakshmidrip.github.io/DROP-Analytics-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Numerical Core - https://lakshmidrip.github.io/DROP-Numerical-Core/
 * 
 * 	DROP Analytics Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Asset Backed Analytics
 * 	- XVA Analytics
 * 	- Exposure and Margin Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Numerical Core implements libraries for the following:
 * 	- Statistical Learning Library
 * 	- Numerical Optimizer Library
 * 	- Machine Learning Library
 * 	- Spline Builder Library
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>LipschitzCoveringNumberBound</i> contains the Upper Bounds of the Covering Numbers induced by Lipschitz
 * and approximate Lipschitz Loss Function Class. The References are:
 * <br><br>
 * <ul>
 * 	<li>
 *  	Anthony, M., and P. L. Bartlett (1999): <i>Artificial Neural Network Learning - Theoretical
 *  		Foundations</i> <b>Cambridge University Press</b> Cambridge, UK
 * 	</li>
 * 	<li>
 *  	Bartlett, P. L., P. Long, and R. C. Williamson (1996): Fat-shattering and the Learnability of Real
 *  		Valued Functions <i>Journal of Computational System Science</i> <b>52 (3)</b> 434-452
 * 	</li>
 * </ul>
 * 
 * <br><br>
 *		<li><b>Module</b>        = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/learning">Learning</a></li>
 *		<li><b>Package</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/learning/bound">Bound</a></li>
 *		<li><b>Specification</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal/StatisticalLearning">Statistical Learning Library</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class LipschitzCoveringNumberBound {
	private double _dblLpUpperBound = java.lang.Double.NaN;
	private double _dblSupremumUpperBound = java.lang.Double.NaN;

	/**
	 * LipschitzCoveringNumberBound Constructor
	 * 
	 * @param dblSupremumUpperBound Supremum Upper Bound for the Covering Number
	 * @param dblLpUpperBound The Lp Upper Bound for the Covering Number
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public LipschitzCoveringNumberBound (
		final double dblSupremumUpperBound,
		final double dblLpUpperBound)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblSupremumUpperBound = dblSupremumUpperBound) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblLpUpperBound = dblLpUpperBound))
			throw new java.lang.Exception ("LipschitzCoveringNumberBound ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Supremum-based Covering Number Upper Bound
	 * 
	 * @return The Supremum-based Covering Number Upper Bound
	 */

	public double supremumUpperBound()
	{
		return _dblSupremumUpperBound;
	}

	/**
	 * Retrieve the Lp-based Covering Number Upper Bound
	 * 
	 * @return The Lp-based Covering Number Upper Bound
	 */

	public double lpUpperBound()
	{
		return _dblLpUpperBound;
	}

	/**
	 * Retrieve the Least Covering Number Upper Bound
	 * 
	 * @return The Least Covering Number Upper Bound
	 */

	public double leastUpperBound()
	{
		return _dblLpUpperBound > _dblSupremumUpperBound ? _dblSupremumUpperBound : _dblLpUpperBound;
	}
}
