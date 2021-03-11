package com.reactiveprogramming.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux(){
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                //When we do this it takes 4 seconds to finally show it in the browser.
                //it happens because browser is not a non-blocking pane
                //it only cares about what format of data is going to be shown
                //so it waits until it gets all the data from the server and then only prints it in the browser.
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    //Providing the return value of stream of JSON value, the browser renders the value one by one
    public Flux<Long> returnFluxStream(){
        return Flux.interval(Duration.ofSeconds(1))  //thi gives use the infinite stream of long
                .log();
    }

    @GetMapping("/mono")
    public Mono<Integer> returnMono(){
        return Mono.just(1)
                .log();
    }
}
