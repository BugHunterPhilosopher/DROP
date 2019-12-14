
package org.drip.measure.gamma;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	and computational support.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
 *  
 *  - DROP Product Core - https://lakshmidrip.github.io/DROP-Product-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Computational Core - https://lakshmidrip.github.io/DROP-Computational-Core/
 * 
 * 	DROP Product Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Loan Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 *  - Asset Liability Management Analytics
 * 	- Capital Estimation Analytics
 * 	- Exposure Analytics
 * 	- Margin Analytics
 * 	- XVA Analytics
 * 
 * 	DROP Computational Core implements libraries for the following:
 * 	- Algorithm Support
 * 	- Computation Support
 * 	- Function Analysis
 *  - Model Validation
 * 	- Numerical Analysis
 * 	- Numerical Optimizer
 * 	- Spline Builder
 *  - Statistical Learning
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Repo Layout Taxonomy     => https://github.com/lakshmiDRIP/DROP/blob/master/Taxonomy.md
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
 * <i>ShapeScaleDistribution</i> implements the Shape and Scale Parameterization of the R<sup>1</sup> Gamma
 * 	Distribution. The References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Devroye, L. (1986): <i>Non-Uniform Random Variate Generation</i> <b>Springer-Verlag</b> New York
 * 		</li>
 * 		<li>
 * 			Gamma Distribution (2019): Gamma Distribution
 * 				https://en.wikipedia.org/wiki/Chi-squared_distribution
 * 		</li>
 * 		<li>
 * 			Louzada, F., P. L. Ramos, and E. Ramos (2019): A Note on Bias of Closed-Form Estimators for the
 * 				Gamma Distribution Derived From Likelihood Equations <i>The American Statistician</i> <b>73
 * 				(2)</b> 195-199
 * 		</li>
 * 		<li>
 * 			Minka, T. (2002): Estimating a Gamma distribution https://tminka.github.io/papers/minka-gamma.pdf
 * 		</li>
 * 		<li>
 * 			Ye, Z. S., and N. Chen (2017): Closed-Form Estimators for the Gamma Distribution Derived from
 * 				Likelihood Equations <i>The American Statistician</i> <b>71 (2)</b> 177-181
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalAnalysisLibrary.md">Numerical Analysis Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/measure/README.md">R<sup>d</sup> Continuous/Discrete Probability Measures</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/measure/gamma/README.md">R<sup>1<sup> Gamma Distribution Implementation/Properties</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class ShapeScaleDistribution
	extends org.drip.measure.continuous.R1Univariate
{
	private double _cdfScaler = java.lang.Double.NaN;
	private double _pdfScaler = java.lang.Double.NaN;
	private double _scaleParameter = java.lang.Double.NaN;
	private double _shapeParameter = java.lang.Double.NaN;
	private org.drip.function.definition.R1ToR1 _gammaEstimator = null;
	private org.drip.function.definition.R1ToR1 _digammaEstimator = null;
	private org.drip.function.definition.R2ToR1 _lowerIncompleteGammaEstimator = null;

	/**
	 * Construct a Gamma Distribution from Shape and Rate Parameters
	 * 
	 * @param shapeParameter Shape Parameter
	 * @param rateParameter Rate Parameter
	 * @param gammaEstimator Gamma Estimator
	 * @param digammaEstimator Digamma Estimator
	 * @param lowerIncompleteGammaEstimator Lower Incomplete Gamma Estimator
	 * 
	 * @return Gamma Distribution from Shape Alpha and Rate Beta Parameters
	 */

	public static final ShapeScaleDistribution ShapeRate (
		final double shapeParameter,
		final double rateParameter,
		final org.drip.function.definition.R1ToR1 gammaEstimator,
		final org.drip.function.definition.R1ToR1 digammaEstimator,
		final org.drip.function.definition.R2ToR1 lowerIncompleteGammaEstimator)
	{
		try
		{
			return new ShapeScaleDistribution (
				shapeParameter,
				1. / rateParameter,
				gammaEstimator,
				digammaEstimator,
				lowerIncompleteGammaEstimator
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ShapeScaleDistribution Constructor
	 * 
	 * @param shapeParameter Shape Parameter
	 * @param scaleParameter Scale Parameter
	 * @param gammaEstimator Gamma Estimator
	 * @param digammaEstimator Digamma Estimator
	 * @param lowerIncompleteGammaEstimator Lower Incomplete Gamma Estimator
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public ShapeScaleDistribution (
		final double shapeParameter,
		final double scaleParameter,
		final org.drip.function.definition.R1ToR1 gammaEstimator,
		final org.drip.function.definition.R1ToR1 digammaEstimator,
		final org.drip.function.definition.R2ToR1 lowerIncompleteGammaEstimator)
		throws java.lang.Exception
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (
				_shapeParameter = shapeParameter
			) || 0. >= _shapeParameter ||
			!org.drip.numerical.common.NumberUtil.IsValid (
				_scaleParameter = scaleParameter
			) || 0. >= _scaleParameter ||
			null == (_gammaEstimator = gammaEstimator) ||
			null == (_digammaEstimator = digammaEstimator) ||
			null == (_lowerIncompleteGammaEstimator = lowerIncompleteGammaEstimator)
		)
		{
			throw new java.lang.Exception (
				"ShapeScaleDistribution Constructor => Invalid Inputs"
			);
		}

		_pdfScaler = (
			_cdfScaler = 1. / _gammaEstimator.evaluate (
				_shapeParameter
			)
		) * java.lang.Math.pow (
			_scaleParameter,
			-1. * _shapeParameter
		);
	}

	/**
	 * Retrieve the Rate Parameter
	 * 
	 * @return The Rate Parameter
	 */

	public double rateParameter()
	{
		return 1. / _scaleParameter;
	}

	/**
	 * Retrieve the Scale Parameter
	 * 
	 * @return The Scale Parameter
	 */

	public double scaleParameter()
	{
		return _scaleParameter;
	}

	/**
	 * Retrieve the Shape Parameter
	 * 
	 * @return The Shape Parameter
	 */

	public double shapeParameter()
	{
		return _shapeParameter;
	}

	/**
	 * Retrieve the Gamma Estimator
	 * 
	 * @return Gamma Estimator
	 */

	public org.drip.function.definition.R1ToR1 gammaEstimator()
	{
		return _gammaEstimator;
	}

	/**
	 * Retrieve the Digamma Estimator
	 * 
	 * @return Digamma Estimator
	 */

	public org.drip.function.definition.R1ToR1 digammaEstimator()
	{
		return _digammaEstimator;
	}

	/**
	 * Retrieve the Lower Incomplete Gamma Estimator
	 * 
	 * @return Lower Incomplete Gamma Estimator
	 */

	public org.drip.function.definition.R2ToR1 lowerIncompleteGammaEstimator()
	{
		return _lowerIncompleteGammaEstimator;
	}

	@Override public double[] support()
	{
		return new double[]
		{
			0.,
			java.lang.Double.POSITIVE_INFINITY
		};
	}

	@Override public double density (
		final double t)
		throws java.lang.Exception
	{
		if (!supported (
			t
		))
		{
			throw new java.lang.Exception (
				"ShapeScaleDistribution::density => Variate not in Range"
			);
		}

		return _pdfScaler * java.lang.Math.pow (
			t,
			_shapeParameter - 1.
		) * java.lang.Math.exp (
			-1. * t / _scaleParameter
		);
	}

	@Override public double cumulative (
		final double t)
		throws java.lang.Exception
	{
		if (!supported (
			t
		))
		{
			throw new java.lang.Exception (
				"ShapeScaleDistribution::cumulative => Invalid Inputs"
			);
		}

		return _cdfScaler * _lowerIncompleteGammaEstimator.evaluate (
			_shapeParameter,
			t / _scaleParameter
		);
	}

	@Override public double mean()
		throws java.lang.Exception
	{
		return _shapeParameter * _scaleParameter;
	}

	@Override public double median()
		throws java.lang.Exception
	{
		throw new java.lang.Exception (
			"ShapeScaleDistribution::median => No Closed Form Available"
		);
	}

	@Override public double mode()
		throws java.lang.Exception
	{
		if (_shapeParameter < 1.)
		{
			throw new java.lang.Exception (
				"ShapeScaleDistribution::mode => No Closed Form Available"
			);
		}

		return (_shapeParameter - 1.) * _scaleParameter;
	}

	@Override public double variance()
		throws java.lang.Exception
	{
		return _shapeParameter * _scaleParameter * _scaleParameter;
	}

	@Override public double skewness()
		throws java.lang.Exception
	{
		return 2. * java.lang.Math.sqrt (1. / _shapeParameter);
	}

	@Override public double excessKurtosis()
		throws java.lang.Exception
	{
		return 6. / _shapeParameter;
	}

	@Override public double differentialEntropy()
		throws java.lang.Exception
	{
		return _shapeParameter + java.lang.Math.log (
			_scaleParameter / _cdfScaler
		) + (1. - _shapeParameter) * _digammaEstimator.evaluate (
			_shapeParameter
		);
	}

	@Override public org.drip.function.definition.R1ToR1 momentGeneratingFunction()
	{
		return new org.drip.function.definition.R1ToR1 (
			null
		)
		{
			@Override public double evaluate (
				final double t)
				throws java.lang.Exception
			{
				if (!org.drip.numerical.common.NumberUtil.IsValid (
						t
					) || t >= 1. / _scaleParameter
				)
				{
					throw new java.lang.Exception (
						"ShapeScaleDistribution::momentGeneratingFunction::evaluate => Invalid Input"
					);
				}

				return java.lang.Math.pow (
					1. - _scaleParameter * t,
					-1. * _shapeParameter
				);
			}
		};
	}
}
