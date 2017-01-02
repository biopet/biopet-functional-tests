package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38

/**
  * Created by Sander Bollen on 29-12-16.
  */

// TODO: set up fixtures and work out
class FreecHSapiensTest extends KopisuSuccess with FreecMethod
class FreecHSapiensFail extends KopisuFail with FreecMethod

class XhmmHSapiensTest extends KopisuSuccess with XhmmMethod
class XhmmHSapeinsFail extends KopisuFail with XhmmMethod

class CnmopsHSapiensTest extends  KopisuSuccess with CnMopsMethod
class CnmopsHSapiensFail extends KopisuFail with CnMopsMethod
