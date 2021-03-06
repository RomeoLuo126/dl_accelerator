package dla.pe

import scala.math.pow
import chisel3.util.log2Ceil

trait MCRENFConfigRS { // contains some scala values, we assume U = 1
  /** this contains the parameters needed in SPad level
    * [[M0]]: weights reuse M0 times
    * [[C0]]: different input feature maps and their weights reuse
    * [[S]]:
    * [[E0]]: same row of weights in a PE
    * [[N0]]: the number of partial sum
    * [[F]]: one row of partial sum
    * [[pSumOneSPadNum]]: the size of one matrix of PSum
    * [[inActMatrixWidth]] and [[inActMatrixHeight]]: the size of input activation Matrix, no Toeplitz
    * [[weightMatrixWidth]] and [[weightMatrixHeight]]: the size of weight Matrix
    * */
  protected val M0: Int = 4
  protected val C0: Int = 2
  protected val S: Int = 4
  protected val E0: Int = 1
  protected val N0: Int = 3
  protected val F: Int = 2
  protected val U: Int = 1
  protected val pSumOneSPadNum: Int = M0*E0*N0*F
  protected val weightMatrixWidth: Int = S*C0 // column
  protected val weightMatrixHeight: Int = M0 // row
  // the matrix below is for matrix-mul the golden model, Toeplitz
  protected val inActMatrixWidth: Int = F*N0*E0 // column
  protected val inActMatrixHeight: Int = S*C0 // row
  protected val slidingInActMatrixWidth: Int = E0*N0
  protected val slidingInActMatrixHeight: Int = C0*(S+U)
}

trait MCRENFConfig { // contains some scala values
  /** this contains the parameters needed in SPad level
    * [[M0]]: weights reuse M0 times
    * [[C0]]: different input feature maps and their weights reuse
    * [[R]]:
    * [[E]]: same row of weights in a PE
    * [[N0]]: the number of partial sum
    * [[F0]]: one row of partial sum
    * [[pSumOneSPadNum]]: the size of one matrix of PSum
    * [[inActMatrixWidth]] and [[inActMatrixHeight]]: the size of input activation Toeplitz Matrix
    * [[weightMatrixWidth]] and [[weightMatrixHeight]]: the size of weight Toeplitz Matrix
    * */
  protected val M0: Int = 4
  protected val C0: Int = 2
  protected val R: Int = 4
  protected val E: Int = 2
  protected val N0: Int = 3
  protected val F0: Int = 1
  protected val pSumOneSPadNum: Int = M0*E*N0*F0
  protected val inActMatrixWidth: Int = F0*N0*E // column
  protected val inActMatrixHeight: Int = R*C0 // row
  protected val weightMatrixWidth: Int = inActMatrixHeight // column
  protected val weightMatrixHeight: Int = M0 // row
  // inActMatrixWidth < inActAdrSize = 9
  // weightMatrixWidth < weightAdrSPadSize = 16
  // C0*R0*E0*N0*F0 <
}

trait SPadSizeConfig {
  protected val pSumDataSPadSize: Int = 32
  protected val inActDataSPadSize: Int = 20
  protected val inActAdrSPadSize: Int = 9 // bigger than inActMatrixWidth + 1
  protected val weightDataSPadSize: Int = 96 // 192 if SIMD
  protected val weightAdrSPadSize: Int = 16 // bigger than weightMatrixWidth + 1
  protected val inActAdrIdxWidth: Int = log2Ceil(inActAdrSPadSize)
  protected val inActDataIdxWidth: Int = log2Ceil(inActDataSPadSize)
  protected val weightAdrIdxWidth: Int = log2Ceil(weightAdrSPadSize)
  protected val weightDataIdxWidth: Int = log2Ceil(weightDataSPadSize)
  protected val pSumDataIdxWidth: Int = log2Ceil(pSumDataSPadSize)
}

trait PESizeConfig {
  protected val inActDataWidth: Int = 12 // 8-bit data and 4-bit count
  /** [[inActAdrWidth]] equals to 4 for test and equals to 7 for modelling */
  protected val inActAdrWidth: Int = 4//7//4
  protected val weightDataWidth: Int = 12 // 24 if SIMD
  protected val weightAdrWidth: Int = 7
  protected val cscDataWidth: Int = 8 // compressed sparse column data width
  protected val cscCountWidth: Int = 4 // compressed sparse column count width
  protected val psDataWidth: Int = 21 //20
  protected val fifoSize: Int = 4
  protected val fifoEn: Boolean = true
  /** when one address vector's element equals to [[inActZeroColumnCode]], then it is a zero column */
  protected val inActZeroColumnCode: Int = pow(2, inActAdrWidth).toInt - 1
  protected val weightZeroColumnCode: Int = pow(2, weightAdrWidth).toInt - 1
}
