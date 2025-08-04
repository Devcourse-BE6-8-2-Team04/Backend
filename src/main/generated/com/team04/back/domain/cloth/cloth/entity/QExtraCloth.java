package com.team04.back.domain.cloth.cloth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QExtraCloth is a Querydsl query type for ExtraCloth
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExtraCloth extends EntityPathBase<ExtraCloth> {

    private static final long serialVersionUID = -1866396278L;

    public static final QExtraCloth extraCloth = new QExtraCloth("extraCloth");

    public final StringPath clothName = createString("clothName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final EnumPath<com.team04.back.domain.weather.weather.enums.Weather> weather = createEnum("weather", com.team04.back.domain.weather.weather.enums.Weather.class);

    public QExtraCloth(String variable) {
        super(ExtraCloth.class, forVariable(variable));
    }

    public QExtraCloth(Path<? extends ExtraCloth> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExtraCloth(PathMetadata metadata) {
        super(ExtraCloth.class, metadata);
    }

}

