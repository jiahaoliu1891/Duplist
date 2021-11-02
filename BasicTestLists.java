public class BasicTestLists {

	public static void main(String args[]) throws InterruptedException {
		int numThr = 10;
		if (args.length > 0) {
			numThr = Integer.parseInt(args[0]);
		}

		Thread threads[] = new Thread[numThr];
		// Create Lists
		List<Integer> optiList = new OptimisticDupList<Integer>();
		List<Integer> lazyList = new LazyDupList<Integer>();
		List<Integer> lockFreeList = new LockFreeDupList<Integer>();
		// Create Tester for each list 
		BasicListTester optiTester = new BasicListTester(numThr, optiList);
		BasicListTester lazyTester = new BasicListTester(numThr, lazyList);
		BasicListTester lockFreeTester = new BasicListTester(numThr, lockFreeList);

		System.out.println("======= TESTNG OPTIMISIC LIST =======");
		for (int i = 0; i < numThr; i++) { threads[i] = new Thread(optiTester); }
		for (int i = 0; i < numThr; i++) { threads[i].start(); }
		try {
			for (int i = 0; i < numThr; i++) { threads[i].join(); }
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}

		System.out.println("======= TESTNG LAZY LIST =======");
		for (int i = 0; i < numThr; i++) { threads[i] = new Thread(lazyTester); }
		for (int i = 0; i < numThr; i++) { threads[i].start(); }
		try {
			for (int i = 0; i < numThr; i++) { threads[i].join(); }
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}

		System.out.println("======= TESTNG LOCK FREE LIST =======");
		for (int i = 0; i < numThr; i++) { threads[i] = new Thread(lockFreeTester); }
		for (int i = 0; i < numThr; i++) { threads[i].start(); }
		try {
			for (int i = 0; i < numThr; i++) { threads[i].join(); }
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
