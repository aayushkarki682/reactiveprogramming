package com.reactiveprogramming.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

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
}
