package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepPairedSkipAllTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=true")

  override def md5SumOutputR1 = Some("b6f564f7496039dfe4e4e9794d191af2")
  override def md5SumOutputR2 = Some("707e26b2fb5a2c999783a2830931f952")

}
