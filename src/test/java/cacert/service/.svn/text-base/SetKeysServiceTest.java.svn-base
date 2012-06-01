package cacert.service;

public class SetKeysServiceTest extends CacertServiceTestCase{

	public SetKeysServiceTest() {super();}
	
	public SetKeysServiceTest(String name){
		super(name);
	}
	
	public void testPublicKeyValue(){
		String pubKey = null;
		pubKey = getCacertPublicKey();
		// Assert
		assertEquals("The CAManager publicKey should be "  + this.publicKey + "but it is " + 
				pubKey, this.publicKey, pubKey);
	}
	
	public void testPrivateKeyValue(){
    	String privKey = null;
		privKey = getCacertPrivateKey();
		// Assert
		assertEquals("The CAManager privateKey should be "  + this.privatekey + "but it is " + 
				privKey, this.privatekey, privKey);
	}
}
