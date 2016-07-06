package votingapp.account;

// this class generate a random string with chosen length made of given characters 
public class RandomStringGenerator {
	public RandomStringGenerator() {
	
	}
	public static String generateRandomString(int length) throws Exception {

		StringBuffer buffer = new StringBuffer();
		String characters = "";

		characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		
		int charactersLength = characters.length();

		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
}

