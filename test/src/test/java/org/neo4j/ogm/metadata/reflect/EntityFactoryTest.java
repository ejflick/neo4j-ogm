/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 *  conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.neo4j.ogm.metadata.reflect;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.domain.canonical.ArbitraryRelationshipEntity;
import org.neo4j.ogm.domain.social.Individual;
import org.neo4j.ogm.exception.core.MappingException;
import org.neo4j.ogm.metadata.MetaData;
import org.neo4j.ogm.response.model.NodeModel;
import org.neo4j.ogm.response.model.RelationshipModel;

/**
 * @author Adam George
 */
public class EntityFactoryTest {

    private EntityFactory entityFactory;

    @Before
    public void setUp() {
        this.entityFactory = new EntityFactory(
            new MetaData("org.neo4j.ogm.domain.social", "org.neo4j.ogm.domain.canonical"));
    }

    @Test
    public void shouldConstructObjectOfParticularTypeUsingItsDefaultZeroArgConstructor() {
        RelationshipModel personRelationshipModel = new RelationshipModel();
        personRelationshipModel.setType("MEMBER_OF");
        ArbitraryRelationshipEntity gary = this.entityFactory.newObject(personRelationshipModel);
        assertThat(gary).isNotNull();

        NodeModel personNodeModel = new NodeModel();
        personNodeModel.setLabels(new String[] { "Individual" });
        Individual sheila = this.entityFactory.newObject(personNodeModel);
        assertThat(sheila).isNotNull();
    }

    @Test
    public void shouldHandleMultipleLabelsSafely() {
        NodeModel personNodeModel = new NodeModel();
        personNodeModel.setLabels(new String[] { "Female", "Individual", "Lass" });
        Individual ourLass = this.entityFactory.newObject(personNodeModel);
        assertThat(ourLass).isNotNull();
    }

    @Test(expected = MappingException.class)
    public void shouldFailIfZeroArgConstructorIsNotPresent() {
        RelationshipModel edge = new RelationshipModel();
        edge.setId(49L);
        edge.setType("ClassWithoutZeroArgumentConstructor");
        this.entityFactory.newObject(edge);
    }

    @Test
    public void shouldBeAbleToConstructObjectWithNonPublicZeroArgConstructor() {
        NodeModel vertex = new NodeModel();
        vertex.setId(163L);
        vertex.setLabels(new String[] { "ClassWithPrivateConstructor" });
        this.entityFactory.newObject(vertex);
    }

    @Test(expected = MappingException.class)
    public void shouldFailForGraphModelComponentWithNoTaxa() {
        NodeModel vertex = new NodeModel();
        vertex.setId(302L);
        vertex.setLabels(new String[0]);
        this.entityFactory.newObject(vertex);
    }

    @Test
    public void shouldConstructObjectIfExplicitlyGivenClassToInstantiate() {
        Individual instance = this.entityFactory.newObject(Individual.class);
        assertThat(instance).as("The resultant instance shouldn't be null").isNotNull();
    }
}
