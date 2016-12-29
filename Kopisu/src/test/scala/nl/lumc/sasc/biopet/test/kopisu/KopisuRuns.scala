package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38

/**
  * Created by Sander Bollen on 29-12-16.
  */

// TODO: set up fixtures and work out
class FreecHSapiensGRCh38Test extends KopisuSuccess with FreecMethod with HsapiensGRCh38
class FreecHSapiensGRCh38Fail extends KopisuFail with FreecMethod with HsapiensGRCh38

class XhmmHSapiensGRCh38Test extends KopisuSuccess with XhmmMethod with HsapiensGRCh38
class XhmmHSapeinsGRCh38Fail extends KopisuFail with XhmmMethod with HsapiensGRCh38

class CnmopsHSapiensGRCh38Test extends  KopisuSuccess with CnMopsMethod with HsapiensGRCh38
class CnmopsHSapiensGRCh38Fail extends KopisuFail with CnMopsMethod with HsapiensGRCh38
