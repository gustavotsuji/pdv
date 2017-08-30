package com.zxventures.pdv_api;
/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxventures.pdv_api.domain.Pdv;
import com.zxventures.pdv_api.repository.PdvRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PdvsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    PdvRepository repository;
    
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MongoTemplate template;
    
    @Test
    public void noParamInSearchIdShouldReturnErrorMessage() throws Exception {

        this.mockMvc.perform(get("/search"))
        .andDo(print())
        .andExpect(status().is5xxServerError());
    }
    
    @Test
    public void noParamInSearchCoordinatesShouldReturnErrorMessage() throws Exception {

        this.mockMvc.perform(get("/search/coordinate"))
        .andDo(print())
        .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void noParamInCreateShouldReturnErrorMessage() throws Exception {

        this.mockMvc.perform(post("/create"))
        .andDo(print())
        .andExpect(status().is4xxClientError());
    }

    @Test
    public void okParamInCreateShouldReturnJsonMessage() throws Exception {

    	String json = "{\"tradingName\":\"Adega da Cerveja - Pinheiros\",\"ownerName\":\"Zé da Silva\",\"document\":\"1432132123891/0001\",\"coverageArea\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]],[[[15.0,5.0],[40.0,10.0],[10.0,20.0],[5.0,10.0],[15.0,5.0]]]]},\"address\":{\"type\":\"Point\",\"coordinates\":[-46.57421,-21.785741]}}";
    	
        this.mockMvc.perform(post("/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());
    }
    
    @Test
    public void okParamInSearchShouldReturnJsonMessage() throws Exception {

    	String json = "{\"tradingName\":\"Adega da Cerveja - Pinheiros\",\"ownerName\":\"Zé da Silva\",\"document\":\"1432132123891/0001\",\"coverageArea\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]],[[[15.0,5.0],[40.0,10.0],[10.0,20.0],[5.0,10.0],[15.0,5.0]]]]},\"address\":{\"type\":\"Point\",\"coordinates\":[-46.57421,-21.785741]}}";
    	
        this.mockMvc.perform(post("/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());
        
        this.mockMvc.perform(post("/search/coordinate")
        		.param("longitude", "35.0")
        		.param("latitude", "35.0"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void toNotFindShouldReturnErrorMessage() throws Exception {

    	String json = "{\"tradingName\":\"Adega da Cerveja - Pinheiros\",\"ownerName\":\"Zé da Silva\",\"document\":\"1432132123891/0001\",\"coverageArea\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]],[[[15.0,5.0],[40.0,10.0],[10.0,20.0],[5.0,10.0],[15.0,5.0]]]]},\"address\":{\"type\":\"Point\",\"coordinates\":[-46.57421,-21.785741]}}";
    	
        this.mockMvc.perform(post("/create")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());
        
        this.mockMvc.perform(post("/search/coordinate")
        		.param("longitude", "0.0")
        		.param("latitude", "0.0"))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    public void checkMultipleCoverageAreaInSearchShouldReturnJsonMessage() throws Exception {

    	String json = "{\"tradingName\":\"Adega da Cerveja - Pinheiros\",\"ownerName\":\"Zé da Silva\",\"document\":\"1432132123891/0001\",\"coverageArea\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]],[[[15.0,5.0],[40.0,10.0],[10.0,20.0],[5.0,10.0],[15.0,5.0]]]]},\"address\":{\"type\":\"Point\",\"coordinates\":[-46.57421,-21.785741]}}";
    	String json1 = "{\"tradingName\":\"Adega da Cerveja2 - Pinheiros\",\"ownerName\":\"Zé da Silva\",\"document\":\"1432132123891/0001\",\"coverageArea\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[30.0,20.0],[45.0,40.0],[10.0,40.0],[30.0,20.0]]],[[[15.0,5.0],[40.0,10.0],[10.0,20.0],[5.0,10.0],[15.0,5.0]]]]},\"address\":{\"type\":\"Point\",\"coordinates\":[35.57421,35.785741]}}";
    	
        Pdv pdv = mapper.readValue(json, Pdv.class);
        Pdv pdv1 = mapper.readValue(json1, Pdv.class);
        
        repository.insert(pdv);
        repository.insert(pdv1);
        //template.indexOps(Pdv.class).ensureIndex( new GeospatialIndex("coverageArea") );
        
        this.mockMvc.perform(get("/search/coordinate")
        		.param("longitude", "35.0")
        		.param("latitude", "35.0"))
        		.andExpect(MockMvcResultMatchers.jsonPath("$.tradingName").value("Adega da Cerveja2 - Pinheiros"));
    }
    
    @After
    public void cleanData() {
    	repository.deleteAll();
    }
}
