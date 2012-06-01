package cacert.service;


public class SetValidPeriodServiceTest extends CacertServiceTestCase{

	public SetValidPeriodServiceTest() {super();}
	
	public SetValidPeriodServiceTest(String name){
		super(name);
	}
	
	public void testValiPeriod(){
    	
    	long period = 0;
		
    	period = getValidPeriod();
		
		// Assert
		assertEquals("The CAManager Valid Period should be "  + this.VALID_PERIOD + "but it is " + 
				period, this.VALID_PERIOD , period);
	}
	
}
