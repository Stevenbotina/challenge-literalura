package com.alura.literalura.model.record;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public record DataSearch(
        @JsonAlias("count") Integer account,
        @JsonAlias("results") List<DataBook> books) {

}
