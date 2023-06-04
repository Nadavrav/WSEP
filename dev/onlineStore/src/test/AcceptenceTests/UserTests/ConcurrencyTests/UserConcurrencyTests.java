package AcceptenceTests.UserTests.ConcurrencyTests;

import ServiceLayer.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserConcurrencyTests {

    @Test
    public void ConcurrencyRegisterTests(){
        ExecutorService executor= Executors.newFixedThreadPool(2000);
        final Service user1=new Service();
        final Service user2=new Service();
        user1.EnterNewSiteVisitor();
        user2.EnterNewSiteVisitor();
        for(int i=1;i<=10;i++){ //username/pass is i,i and -i,-i - so register & login should always succeed and have no duplicates/invalid lengths
            final String finalUser1="Concurrency Test User "+i;
            //   final String finalUser2=Integer.toString(-i);
            final String finalPass1="My password is: "+ i; //assure length is larger then 8
            final String finalPass2="My password is: "+ -i;
            Future<Boolean> f1=executor.submit(() -> user1.Register(finalUser1,finalPass1).isError());
            Future<Boolean> f2=executor.submit(() -> user2.Register(finalUser1,finalPass2).isError());
            try {
                boolean r1=f1.get();
                boolean r2=f2.get();
                Assertions.assertTrue( (r1 & !r2) | (!r1 & r2)); //expecting exactly one of them to fail and one to pass
            }
            catch (Exception e){
                Assertions.fail("Thread Error - see exception type doc");
            }
        }
        executor.shutdown();
    }
    @Test
    public void ConcurrencyLoginTests(){
        ExecutorService executor= Executors.newFixedThreadPool(2000);
        final Service user1=new Service();
        final Service user2=new Service();
        user1.EnterNewSiteVisitor();
        user2.EnterNewSiteVisitor();
        for(int i=1;i<=10;i++){ //username/pass is i,i and -i,-i - so register & login should always succeed and have no duplicates/invalid lengths
            final String finalUser1="login Test User: "+i;
            final String finalPass1="My password is: "+ i; //assure length is larger then 8
            user1.Register(finalUser1,finalPass1);
            Future<Boolean> f1=executor.submit(() -> user1.login(finalUser1,finalPass1).isError());
            Future<Boolean> f2=executor.submit(() -> user2.login(finalUser1,finalPass1).isError());
            try {
                boolean r1=f1.get();
                boolean r2=f2.get();
                Assertions.assertTrue( (r1 & !r2) | (!r1 & r2)); //expecting exactly one of them to fail and one to pass
            }
            catch (Exception e){
                Assertions.fail("Thread Error - see exception type doc");
            }
        }
        executor.shutdown();
    }
}
