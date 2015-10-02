package nl.lumc.sasc.biopet.test.flexiprep

/**
 * Created by pjvan_thof on 10/2/15.
 */
class FlexiprepPairedClipTest extends FlexiprepPaired {

  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=true")

  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")
  override def md5SumOutputR2 = Some("706f9a97239d1e2110d7b48605559d22")

}

class FlexiprepPairedClipTrimTest extends FlexiprepPaired {

  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=false")

  override def md5SumOutputR1 = Some("b88c68ac883086d558a76cd2fa9252fc")
  override def md5SumOutputR2 = Some("2acfae7d3ead4c6054786a1b5eef2b17")

}

class FlexiprepPairedSkipAllTest extends FlexiprepPaired {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=true")

  override def md5SumOutputR1 = Some("b6f564f7496039dfe4e4e9794d191af2")
  override def md5SumOutputR2 = Some("707e26b2fb5a2c999783a2830931f952")

}

class FlexiprepPairedTrimTest extends FlexiprepPaired {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=false")

  override def md5SumOutputR1 = Some("1a468148f163dfc8cd46e479706efadd")
  override def md5SumOutputR2 = Some("13cc49a8e8203a634e7bbe07723f13b7")

}
