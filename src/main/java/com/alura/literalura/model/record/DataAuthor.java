package com.alura.literalura.model.record;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DataAuthor(
        @JsonAlias("name") String name,
        @JsonAlias("birth_year") Integer birthYear,
        @JsonAlias("death_year") Integer deathYear){

}
