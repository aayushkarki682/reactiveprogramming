package com.reactiveprogramming.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class fluxAndMonoTest {

    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Programming")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("After Error"))   //After the error has occurred the Flux will not send any more data to the subscriber
                .log();
        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println(e),
                        () -> System.out.println("Completed")); //Subscriber method has another parameter, in which it gets executed whenever the
                                                                //program or flux runs successfully with onComplete() event.
    }

    @Test
    public void fluxTestElementsWithoutError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Programming")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Programming")
                .verifyComplete(); //verifyComplete() is a call which is equivalent to subscribe, and that's the
                                    //one which starts the flow from Flux to Subscriber
    }

    @Test
    public void fluxTestElementsWithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Programming")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Programming")
                .expectError(RuntimeException.class)
                //.expectErrorMessage("Exception occurred")
                .verify();  //if we expect an error in the Flux, then we should use the verify method to start the subscription process
    }

    @Test
    public void fluxTestElementsCountWithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Programming")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                //.expectErrorMessage("Exception occurred")
                .verify();
    }

    @Test
    public void fluxTestElementsWithError1(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Programming")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring","Spring Boot","Reactive Programming")
                .expectError(RuntimeException.class)
                //.expectErrorMessage("Exception occurred")
                .verify();
    }
}
