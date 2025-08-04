package com.team04.back.domain.cloth.cloth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QClothInfo is a Querydsl query type for ClothInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClothInfo extends EntityPathBase<ClothInfo> {

    private static final long serialVersionUID = 712031304L;

    public static final QClothInfo clothInfo = new QClothInfo("clothInfo");

    public final EnumPath<com.team04.back.domain.cloth.cloth.enums.Category> category = createEnum("category", com.team04.back.domain.cloth.cloth.enums.Category.class);

    public final StringPath clothName = createString("clothName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Double> maxFeelsLike = createNumber("maxFeelsLike", Double.class);

    public final NumberPath<Double> minFeelsLike = createNumber("minFeelsLike", Double.class);

    public QClothInfo(String variable) {
        super(ClothInfo.class, forVariable(variable));
    }

    public QClothInfo(Path<? extends ClothInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClothInfo(PathMetadata metadata) {
        super(ClothInfo.class, metadata);
    }

}

