package hw_concurrency;

public class Main {
	
	private final int ITERATIONS = 100;
	private final int STUDENTS_INITIALLY = 50;
	private final int STUDENTS_EACH_ITERATION = 5;
	
	public Main() {
		
	}
	
	public static void main(String[] args) {
		Main schoolFestival = new Main();
		schoolFestival.run();
	}
	
	private void run() {
		init();
		prepare();
		start();
	}
	
	private void init() {
		School seirin = EntityFactory.getSchool("Seirin High School");
		EntityFactory.createStand("Cloudy Candy", "Sells cotton Candy", 4.7, seirin);
		EntityFactory.createStand("Harmony Karaoke", "Do karaoke with Mitobe", 4.2, seirin);
		EntityFactory.createStand("Caffeine Charger", "Sells coffee", 2.2, seirin);
		EntityFactory.createStand("Virtual Guitar", "Play virtual guitar", 6.0, seirin);
		EntityFactory.createStand("InterStreet", "Play street basketball with Seirin Basketball Team", 7.0, seirin);
		
		School honnouji = EntityFactory.getSchool("Honnouji Academy");
		EntityFactory.createStand("Capypa Fireworks", "Sells fireworks", 2.8, honnouji);
		EntityFactory.createStand("Ground Cherry Lanterns", "Sells lanterns", 3.4, honnouji);
		EntityFactory.createStand("Ramen Ichiraku", "Sells ramen", 3.3, honnouji);
		EntityFactory.createStand("Lambo’s Takoyaki", "Sells takoyaki", 5.0, honnouji);
		EntityFactory.createStand("Ultima Wars", "Challenge Satsuki or Ryoko with an ultima uniform", 7.3, honnouji);
		
		School ultiHackers = EntityFactory.getSchool("UltiHackers Academy");
		EntityFactory.createStand("Engineer’s pizza", "Sells pizza", 3.8, ultiHackers);
		EntityFactory.createStand("Ice Breaker Hunt", "Dive in the matrix to hack a company", 4.4, ultiHackers);
		EntityFactory.createStand("MCP’s Light Cycle", "Virtual Death Match on the grid with light cycles", 3.5, ultiHackers);
		EntityFactory.createStand("Dragon Hunters", "Sells dragon horn", 6.4, ultiHackers);
		EntityFactory.createStand("Umbrella Corporation", "Sells umbrella", 5.4, ultiHackers);
		
		School jediPraxeum = EntityFactory.getSchool("Jedi Praxeum");
		EntityFactory.createStand("Jedi’s place", "Receive Jedi training from Yoda", 8.8, jediPraxeum);
		EntityFactory.createStand("Dark Cookies", "Sells Darth Vader’s homemade cookies", 1.4, jediPraxeum);
		EntityFactory.createStand("Ring Throwing at Yoda’s Place", "Ring throwing training with Yoda", 3.5, jediPraxeum);
		EntityFactory.createStand("Skywalker Waffles", "Sells Luke’s homemade waffles", 7.2, jediPraxeum);
		
		School localCommittee = EntityFactory.getSchool("Festival’s Local Committee");
		EntityFactory.createStand("Bookworm’s Place", "Sells books", 1.8, localCommittee);
		EntityFactory.createStand("Rainbow Pots", "Sells flowers in pots", 2.0, localCommittee);
		EntityFactory.createStand("Killua’s Deadly Dart", "Play dart", 3.2, localCommittee);
		EntityFactory.createStand("Coyote Starrk’s Pistol Game", "Shoot to earn souvenirs", 5.5, localCommittee);
	}
	
	private void prepare() {
		loadItemPackToStands();
		addStudents(STUDENTS_INITIALLY);
	}
	
	private void start() {
		for (int i = 0; i < ITERATIONS; i++) {
			System.out.println("Iteration " + (i + 1) + " / " + ITERATIONS);
			loadItemPackToStands();
			addStudents(STUDENTS_EACH_ITERATION);
			reportStatus();
		}
		
		finish();
	}
	
	private void finish() {
		EntityFactory.printStatus();
		
		double max = 0;
		int index = 0;
		
		for (int i = 0; i < EntityFactory.getStands().size(); i++) {
			if (EntityFactory.getStands().get(i).getTotalRevenue() > max) {
				max = EntityFactory.getStands().get(i).getTotalRevenue();
				index = i;
			}
		}
		
		EntityFactory.getStands().get(index).win();
	}
	
	private void loadItemPackToStands() {
		for (Stand stand : EntityFactory.getStands()) {
			stand.loadItemPack();
		}
	}
	
	private void addStudents(int students) {
		for (int i = 0; i < students; i++) {
			EntityFactory.createStudent();
		}
	}
	
	private void reportStatus() {
		for (Stand stand : EntityFactory.getStands()) {
			stand.printStatus();
		}
		EntityFactory.printStatus();
	}

}
