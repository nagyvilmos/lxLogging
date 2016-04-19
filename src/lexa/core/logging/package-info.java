/**
 * Support for logging in the Lexa system.
 *
 * <p>Classes create an instance of the Logger which will write to the
 * currently defined output.  By default this will be {@link System#out}.
 *
 * <p>The logging will be to {@code System.out} unless otherwise specified.  To use
 * the logger, an instance is created for the messages:
 *
 * <pre>
 * public class MyClass {
 *   // Logger instance
 *   private lexa.core.logging.Logger logger;
 *
 *   public MyClass (String name) {
 *     this.logger = new lexa.core.logging.Logger("MyClass", name);
 *     // ...
 *   }
 * }
 * </pre>
 *
 * <p>There is one log file within the JVM, this can be changed from {@code System.out}
 * at start up:
 * <pre>
 * public class MyApplication {
 *   public void main (String[] args) {
 *     try {
 *       setLogWriter(new File("logfile.log"));
 *     } catch (FileNotFoundException ex) {
 *       setLogWriter(System.out);
 *       new Logger("MyApplication","main").error("Cannot set log file.", ex);
 *       return;
 *     }
 *     // ...
 *   }
 * }
 * </pre>
 *
 * @since 2013-05
 */
package lexa.core.logging;
