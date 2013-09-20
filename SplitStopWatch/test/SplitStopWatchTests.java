import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * This is the test class for the SplipStopWatch - tool.
 * 
 * @author UNGE
 */
public class SplitStopWatchTests {

    /**
     * This method is called every time a test is started.
     */
    @Before
    public void Initialize() {
    }

    /**
     * This method is called every time a test is completed.
     */
    @After
    public void TearDown() {
    }

    /**
     * Test the splitStopWatch by outputting a sample trace. Please verify it manually.
     */
    @Test
    public void SplitStopWatchBehaviorTest_StandardPrecision() {
        SplitStopWatch ssw = new SplitStopWatch();

        try {
            System.out.println("TEST 1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 1.1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 1.2");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2.1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2.2");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2.3");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 3");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.splitAndStop("split and stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 3.1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.splitAndStop("split and stopped.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.splitAndStop("split and stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the splitStopWatch by outputting a sample trace. Please verify it manually.
     */
    @Test
    public void SplitStopWatchBehaviorTest_NanoPrecision() {
        SplitStopWatch ssw = new SplitStopWatch();
        ssw.setNanoPrecision(true);

        try {
            System.out.println("TEST 1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 1.1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 1.2");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2.1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2.2");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 2.3");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.split("split.");
            Thread.sleep(10);
            ssw.stop("stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 3");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.splitAndStop("split and stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();

            System.out.println("TEST 3.1");
            ssw.start("started.");
            Thread.sleep(10);
            ssw.splitAndStop("split and stopped.");
            Thread.sleep(10);
            ssw.start("started.");
            Thread.sleep(10);
            ssw.splitAndStop("split and stopped.");
            Thread.sleep(10);
            ssw.reset("reset.");
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}