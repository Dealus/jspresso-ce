<#macro generateClassHeader componentDescriptor>
  <#local package=componentDescriptor.name[0..componentDescriptor.name?last_index_of(".")-1]/>
  <#local componentName=componentDescriptor.name[componentDescriptor.name?last_index_of(".")+1..]/>
  <#local superInterfaceList=[]/>
  <#global isEntity=componentDescriptor.entity/>
  <#if componentDescriptor.ancestorDescriptors?exists>
    <#list componentDescriptor.ancestorDescriptors as ancestorDescriptor>
      <#if "org.jspresso.framework.model.entity.IEntity" != ancestorDescriptor.name>
	      <#local superInterfaceList=superInterfaceList + [ancestorDescriptor.name]/>
	      <#if ancestorDescriptor.entity>
	        <#local superEntity=ancestorDescriptor/>
	        <#if superEntity.sqlName?exists>
	          <#local superEntityTableName=superEntity.sqlName/>
	        <#else>  
	          <#local superEntityName=superEntity.name[superEntity.name?last_index_of(".")+1..]/>
	          <#local superEntityTableName=generateSQLName(superEntityName)/>
	        </#if>
	        <#local idDescriptor = componentDescriptor.getPropertyDescriptor("id")/>
	        <#if idDescriptor.sqlName?exists>
            <#local idColumnName=idDescriptor.sqlName/>
	        <#else>  
	          <#local idColumnName=generateSQLName(idDescriptor.name)/>
	        </#if>
	      </#if>
	    </#if>
    </#list>
    <#if isEntity && !(superEntity?exists)>
      <#local superInterfaceList = ["org.jspresso.framework.model.entity.IEntity"] + superInterfaceList/>
    </#if>
  </#if>
  <#if componentDescriptor.serviceContractClassNames?exists>
    <#list componentDescriptor.serviceContractClassNames as serviceContractClassName>
      <#local superInterfaceList=superInterfaceList + [serviceContractClassName]/>
    </#list>
  </#if>
  <#if componentDescriptor.sqlName?exists>
    <#global tableName=componentDescriptor.sqlName/>
  <#else>  
    <#global tableName=generateSQLName(componentName)/>
  </#if>
/*
 * Generated by Jspresso. All rights reserved.
 */
package ${package};

/**
 * ${componentName} <#if isEntity>entity<#else>component</#if>.
 * <p>
 * Generated by Jspresso. All rights reserved.
 * <p>
 *
  <#if isEntity>
 * @hibernate.mapping
 *           default-access = "org.jspresso.framework.model.persistence.hibernate.property.EntityPropertyAccessor"
 *           default-cascade="persist,merge,save-update"
    <#if superEntity?exists>
 * @hibernate.joined-subclass
    <#else>
 * @hibernate.class
    </#if>
 *           table = "${tableName}"
 *           dynamic-insert = "true"
 *           dynamic-update = "true"
 *           persister =
 *            "org.jspresso.framework.model.persistence.hibernate.entity.persister.EntityProxyJoinedSubclassEntityPersister"
    <#if componentDescriptor.purelyAbstract>
 *           abstract = "true"
    </#if>
    <#if superEntity?exists>
 * @hibernate.joined-subclass-key
 *           column = "${idColumnName}"
    </#if>
  </#if>
 * @author Generated by Jspresso
 * @version $LastChangedRevision$
 */
@SuppressWarnings("all")
public interface ${componentName}<#if (superInterfaceList?size > 0)> extends
<#list superInterfaceList as superInterface>  ${superInterface}<#if superInterface_has_next>,${"\n"}<#else> {</#if></#list>
<#else> {
</#if>
  <#if isEntity && !superEntity?exists>
    <@generateScalarGetter componentDescriptor=componentDescriptor propertyDescriptor=componentDescriptor.getPropertyDescriptor("id")/>
    <@generateScalarGetter componentDescriptor=componentDescriptor propertyDescriptor=componentDescriptor.getPropertyDescriptor("version")/>
  </#if>

</#macro>

<#macro generateScalarSetter componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#if propertyDescriptor.modelType.array>
    <#local propertyType=propertyDescriptor.modelType.componentType.name+"[]"/>
  <#else>
    <#local propertyType=propertyDescriptor.modelType.name/>
  </#if>
  /**
   * Sets the ${propertyName}.
   *
   * @param ${propertyName}
   *          the ${propertyName} to set.
   */
  void set${propertyName?cap_first}(${propertyType} ${propertyName});

</#macro>

<#macro generateScalarGetter componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#if propertyDescriptor.name ="id">
    <#local propertyType="java.io.Serializable"/>
  <#elseif propertyDescriptor.modelType.array>
    <#local propertyType=propertyDescriptor.modelType.componentType.name+"[]"/>
  <#else>
    <#local propertyType=propertyDescriptor.modelType.name/>
  </#if>
  <#if propertyDescriptor.sqlName?exists>
    <#local columnName=propertyDescriptor.sqlName/>
    <#local columnNameGenerated = false/>
  <#else>  
    <#local columnName=generateSQLName(propertyName)/>
    <#local columnNameGenerated = true/>
  </#if>
  /**
   * Gets the ${propertyName}.
   *
  <#if !propertyDescriptor.computed>
    <#if propertyDescriptor.name ="id">
   * @hibernate.id
   *           generator-class = "assigned"
      <#if hibernateTypeRegistry.getRegisteredType(propertyDescriptor.modelType.name)?exists>
        <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor")>
          <#assign idTypeName="org.jspresso.framework.model.persistence.hibernate.entity.type.ByteArrayType"/>
        <#else> 
          <#local hibernateType=hibernateTypeRegistry.getRegisteredType(propertyDescriptor.modelType.name)/>
          <#if hibernateType?exists>
            <#assign idTypeName=hibernateType.name/>
          </#if>
        </#if>
        <#local hibernateTypeName=idTypeName/>
      </#if>
      <#if hibernateTypeName?exists>
   *           type = "${hibernateTypeName}"
      <#else>
   *           type = "string"
      </#if>
    <#elseif propertyDescriptor.name ="version">
   * @hibernate.version
   *           unsaved-value = "null"
   *           column = "${columnName}"
   *           type = "integer"
    <#else>
   * @hibernate.property
    </#if>
    <#if !propertyDescriptor.versionControl>
   *           optimistic-lock = "false"
    </#if>
    <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IDatePropertyDescriptor")>
      <#if propertyDescriptor.type = "DATE_TIME">
   *           type = "timestamp"
      <#else>
   *           type = "date"
      </#if>
    <#elseif instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.ITimePropertyDescriptor")>
   *           type = "time"
<#-- <#elseif    instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor")
              && !(propertyDescriptor.maxLength?exists)>
   *           type = "blob"
-->
    </#if>
   * @hibernate.column
   *           name = "${columnName}"
    <#if (   instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IStringPropertyDescriptor")
          || instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor")
          || instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor")
         )
      && (propertyDescriptor.maxLength?exists)>
      <#if propertyDescriptor.name ="id">
        <#assign idTypeLength = propertyDescriptor.maxLength>
      </#if>
   *           length = "${propertyDescriptor.maxLength?c}"
    <#elseif instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IColorPropertyDescriptor")>
   *           length = "10"
    </#if>
    <#if (   propertyDescriptor.mandatory
          || instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor"))>
   *           not-null = "true"
    </#if>
    <#if propertyDescriptor.unicityScope?exists>
   *           unique-key = "${generateSQLName(propertyDescriptor.unicityScope)}_UNQ"
    </#if>
    <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.INumberPropertyDescriptor")>
      <#if (propertyDescriptor.minValue?exists)
         &&(propertyDescriptor.maxValue?exists)>
        <#local infLength=propertyDescriptor.minValue?int?c?length/>
        <#local supLength=propertyDescriptor.maxValue?int?c?length/>
        <#if (infLength > supLength)>
          <#local length=infLength/>
        <#else>
          <#local length=supLength/>
        </#if>
   *           precision = "${length?c}"
      <#else>
   *           precision = "10"
      </#if>
      <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor")>
        <#if propertyDescriptor.maxFractionDigit?exists>
   *           scale = "${propertyDescriptor.maxFractionDigit?c}"
        <#else>
   *           scale = "2"
        </#if>
      </#if>
    </#if>
  <#elseif propertyDescriptor.sqlName?exists>
   * @hibernate.property
   *           formula = "${propertyDescriptor.sqlName}"
  </#if>
   * @return the ${propertyName}.
   */
  <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor")>
  ${propertyType} is${propertyName?cap_first}();
  <#else>
  ${propertyType} get${propertyName?cap_first}();
  </#if>

</#macro>

<#macro generateCollectionSetter componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#local collectionType=propertyDescriptor.modelType.name/>
  <#local elementType=propertyDescriptor.referencedDescriptor.elementDescriptor.name/>
  /**
   * Sets the ${propertyName}.
   *
   * @param ${propertyName}
   *          the ${propertyName} to set.
   */
  void set${propertyName?cap_first}(${collectionType}<${elementType}> ${propertyName});

</#macro>

<#macro generateCollectionAdder componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#local elementType=propertyDescriptor.referencedDescriptor.elementDescriptor.name/>
  /**
   * Adds an element to the ${propertyName}.
   *
   * @param ${propertyName}Element
   *          the ${propertyName} element to add.
   */
  void addTo${propertyName?cap_first}(${elementType} ${propertyName}Element);
  <#if propertyDescriptor.modelType.name = "java.util.List">

  /**
   * Adds an element to the ${propertyName} at the specified index. If the index is out
   * of the list bounds, the element is simply added at the end of the list.
   *
   * @param index
   *          the index to add the ${propertyName} element at.
   * @param ${propertyName}Element
   *          the ${propertyName} element to add.
   */
  void addTo${propertyName?cap_first}(int index, ${elementType} ${propertyName}Element);
  </#if>

</#macro>

<#macro generateCollectionRemer componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#local elementType=propertyDescriptor.referencedDescriptor.elementDescriptor.name/>
  /**
   * Removes an element from the ${propertyName}.
   *
   * @param ${propertyName}Element
   *          the ${propertyName} element to remove.
   */
  void removeFrom${propertyName?cap_first}(${elementType} ${propertyName}Element);

</#macro>

<#macro generateCollectionGetter componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#if propertyDescriptor.fkName?exists>
    <#local fkName=propertyDescriptor.fkName/>
  </#if>
  <#local collectionType=propertyDescriptor.modelType.name/>
  <#local elementDescriptor=propertyDescriptor.referencedDescriptor.elementDescriptor/>
  <#local elementIsEntity=elementDescriptor.entity/>
  <#local elementType=propertyDescriptor.referencedDescriptor.elementDescriptor.name/>
  <#local componentName=componentDescriptor.name[componentDescriptor.name?last_index_of(".")+1..]/>
  <#local elementName=elementType[elementType?last_index_of(".")+1..]/>
  <#local isEntity=componentDescriptor.entity/>
  <#local isElementEntity=elementDescriptor.entity/>
  <#if collectionType="java.util.List">
    <#local hibernateCollectionType="list"/>
  <#elseif collectionType="java.util.Set">
    <#local hibernateCollectionType="set"/>
  </#if>
  <#local manyToMany=propertyDescriptor.manyToMany/>
  <#if propertyDescriptor.reverseRelationEnd?exists>
    <#local bidirectional=true/>
    <#local reversePropertyName=propertyDescriptor.reverseRelationEnd.name/>
    <#local reverseMandatory=propertyDescriptor.reverseRelationEnd.mandatory/>
    <#if propertyDescriptor.reverseRelationEnd.fkName?exists>
      <#local reverseFkName=propertyDescriptor.reverseRelationEnd.fkName/>
    </#if>
    <#if manyToMany>
      <#--
      <#if (compareStrings(elementName, componentName) != 0)>
        <#local inverse=(compareStrings(elementName, componentName) > 0)/>
      <#else>
        Reflexive many to many
        <#local inverse=(compareStrings(propertyName, reversePropertyName) > 0)/>
      </#if>
      -->
      <#local inverse = !propertyDescriptor.leadingPersistence/>
    <#else>
      <#if hibernateCollectionType="list">
        <#local inverse=false/>
      <#else>
        <#local inverse=true/>
      </#if>
    </#if>
  <#else>
    <#local bidirectional=false/>
    <#local inverse=false/>
    <#if !manyToMany>
      <#local reversePropertyName=propertyName+componentName/>
    </#if>
  </#if>
  <#if componentDescriptor.sqlName?exists>
    <#local compSqlName=componentDescriptor.sqlName/>
  <#else>  
    <#local compSqlName=generateSQLName(componentName)/>
  </#if>
  <#if elementDescriptor.sqlName?exists>
    <#local eltSqlName=elementDescriptor.sqlName/>
  <#else>  
    <#local eltSqlName=generateSQLName(elementName)/>
  </#if>
  <#if propertyDescriptor.sqlName?exists>
    <#local propSqlName=propertyDescriptor.sqlName/>
    <#local propSqlNameGenerated = false/>
  <#else>  
    <#local propSqlName=generateSQLName(propertyName)/>
    <#local propSqlNameGenerated = true/>
  </#if>
  <#local revSqlNameGenerated = true/>
  <#if propertyDescriptor.reverseRelationEnd?exists>
	  <#if propertyDescriptor.reverseRelationEnd.sqlName?exists>
	    <#local revSqlName=propertyDescriptor.reverseRelationEnd.sqlName/>
      <#local revSqlNameGenerated = false/>
	  <#else>  
	    <#local revSqlName=generateSQLName(reversePropertyName)/>
	  </#if>
	<#else>
	  <#local revSqlName=propSqlName+"_"+compSqlName/>
	</#if>
  /**
   * Gets the ${propertyName}.
   *
  <#if !propertyDescriptor.computed>
   * @hibernate.${hibernateCollectionType}
    <#if propertyDescriptor.fetchType?exists>
      <#if propertyDescriptor.fetchType.toString() = "JOIN">
   *           fetch = "join"
      <#elseif propertyDescriptor.fetchType.toString() = "SUBSELECT">
   *           fetch = "subselect"
      </#if>
    </#if>
    <#if propertyDescriptor.batchSize?exists>
   *           batch-size = "${propertyDescriptor.batchSize?c}"
    </#if>
    <#if !propertyDescriptor.versionControl>
   *           optimistic-lock = "false"
    </#if>
    <#if propertyDescriptor.composition>
   *           cascade = "persist,merge,save-update,delete"
    </#if>
    <#if manyToMany>
      <#if inverse>
        <#local joinTableName=eltSqlName+"_"+revSqlName/>
   *           table = "${joinTableName}"
      <#else>
        <#local joinTableName=compSqlName+"_"+propSqlName/>
   *           table = "${joinTableName}"
      </#if>
      <#--<#local dedupAliasPrefix=compactString(joinTableName)/>-->
      <#local dedupAliasPrefix=""/>
    </#if>
    <#if inverse>
   *           inverse = "true"
    </#if>
<#--
  The following replaces the previous block wich makes hibernate fail... Ordering is now handled in the entity itself.
  But hibernate must be provided with an ordering attribute so that a Linked HashSet is used instead of a set but if
  and only if the referenced collection conains entites.
  Well, the following is bad. It kills performance on joined criteria query and it get us into the Hibernate bug
  HHH-7116.
    <#if (elementIsEntity && !manyToMany && !(hibernateCollectionType="list"))>
   *           order-by="ID"
    </#if>
-->
    <#if manyToMany>
   * @hibernate.key
      <#if componentName=elementName>
        <#if inverse>
   *           column = "2${dedupAliasPrefix}${compSqlName}_ID2"
          <#if fkName?exists>
   *           foreign-key = "${fkName}"
          <#else>
   *           foreign-key = "${joinTableName}_${compSqlName}_FK2"
          </#if>
        <#else>
   *           column = "1${dedupAliasPrefix}${compSqlName}_ID1"
          <#if fkName?exists>
   *           foreign-key = "${fkName}"
          <#else>
   *           foreign-key = "${joinTableName}_${compSqlName}_FK1"
          </#if>
        </#if>
      <#else>
   *           column = "${dedupAliasPrefix}${compSqlName}_ID"
        <#if fkName?exists>
   *           foreign-key = "${fkName}"
        <#else>
   *           foreign-key = "${joinTableName}_${compSqlName}_FK"
        </#if>
      </#if>
   * @hibernate.many-to-many
   *           class = "${elementType}"
      <#if componentName=elementName>
        <#if inverse>
   *           column = "1${dedupAliasPrefix}${eltSqlName}_ID1"
          <#if reverseFkName?exists>
   *           foreign-key = "${reverseFkName}"
          <#else>
   *           foreign-key = "${joinTableName}_${eltSqlName}_FK1"
          </#if>
        <#else>
   *           column = "2${dedupAliasPrefix}${eltSqlName}_ID2"
          <#if reverseFkName?exists>
   *           foreign-key = "${reverseFkName}"
          <#else>
   *           foreign-key = "${joinTableName}_${eltSqlName}_FK2"
          </#if>
        </#if>
      <#else>
   *           column = "${dedupAliasPrefix}${eltSqlName}_ID"
        <#if reverseFkName?exists>
   *           foreign-key = "${reverseFkName}"
        <#else>
   *           foreign-key = "${joinTableName}_${eltSqlName}_FK"
        </#if>
      </#if>
    <#else>
   * @hibernate.key
      <#if revSqlNameGenerated>
   *           column = "${revSqlName}_ID"
      <#else>
   *           column = "${revSqlName}"
      </#if>
      <#if bidirectional>
        <#if reverseMandatory>
   *           not-null = "true"
        </#if>
      </#if>
      <#if isEntity>
        <#if bidirectional>
          <#if fkName?exists>
   *           foreign-key = "${fkName}"
          </#if>
        <#else>
          <#if fkName?exists>
   *           foreign-key = "${fkName}"
          <#else>
   *           foreign-key = "${revSqlName}_FK"
          </#if>
        </#if>
      <#else>
   *           foreign-key = "none"
      </#if>
      <#if isElementEntity>
   * @hibernate.one-to-many
   *           class = "${elementType}"
      <#else>
   * @hibernate.composite-element
   *           class = "${elementType}"
      </#if>
    </#if>
    <#if hibernateCollectionType="list">
   * @hibernate.list-index
   *           column = "${compSqlName}_${propSqlName}_SEQ"
    </#if>
  </#if>
   * @return the ${propertyName}.
   */
  <#if generateAnnotations>
  @org.jspresso.framework.util.bean.ElementClass(${elementType}.class)
  </#if>
  ${collectionType}<${elementType}> get${propertyName?cap_first}();

</#macro>

<#macro generateEntityRefSetter componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#local propertyType=propertyDescriptor.referencedDescriptor.name/>
  /**
   * Sets the ${propertyName}.
   *
   * @param ${propertyName}
   *          the ${propertyName} to set.
   */
  void set${propertyName?cap_first}(${propertyType} ${propertyName});
  
</#macro>

<#macro generateComponentRefGetter componentDescriptor propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#if propertyDescriptor.fkName?exists>
    <#local fkName=propertyDescriptor.fkName/>
  </#if>
  <#local propertyType=propertyDescriptor.referencedDescriptor.name/>
  <#if propertyDescriptor.referencedDescriptor.sqlName?exists>
    <#local refSqlName=propertyDescriptor.referencedDescriptor.sqlName/>
  <#else>  
    <#local refSqlName=generateSQLName(propertyDescriptor.referencedDescriptor.name[propertyDescriptor.referencedDescriptor.name?last_index_of(".")+1..])/>
  </#if>
  <#local isReferenceEntity=propertyDescriptor.referencedDescriptor.entity/>
  <#local isPurelyAbstract=propertyDescriptor.referencedDescriptor.purelyAbstract/>
  <#local oneToOne=propertyDescriptor.oneToOne/>
  <#local composition=propertyDescriptor.composition/>
  <#if propertyDescriptor.reverseRelationEnd?exists>
    <#local bidirectional=true/>
    <#local reversePropertyName=propertyDescriptor.reverseRelationEnd.name/>
    <#if instanceof(propertyDescriptor.reverseRelationEnd, "org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor")>
      <#local componentName=componentDescriptor.name[componentDescriptor.name?last_index_of(".")+1..]/>
      <#local elementName=propertyType[propertyType?last_index_of(".")+1..]/>
      <#--
      <#if (compareStrings(elementName, componentName) != 0)>
        <#local reverseOneToOne=(compareStrings(elementName, componentName) < 0)/>
      <#else>
        Reflexive one to one
        <#local reverseOneToOne=(compareStrings(propertyName, reversePropertyName) < 0)/>
      </#if>
      -->
      <#local reverseOneToOne = !propertyDescriptor.leadingPersistence/>
    <#else>
      <#local reverseOneToOne=false/>
      <#if propertyDescriptor.reverseRelationEnd.modelType.name="java.util.List">
        <#local managesPersistence=false/>
      <#else>
        <#local managesPersistence=true/>
      </#if>
    </#if>
  <#else>
    <#local bidirectional=false/>
    <#local reverseOneToOne=false/>
  </#if>
  <#if propertyDescriptor.sqlName?exists>
    <#local propSqlName=propertyDescriptor.sqlName/>
    <#local propSqlNameGenerated = false/>
  <#else>  
    <#local propSqlName=generateSQLName(propertyName)/>
    <#local propSqlNameGenerated = true/>
  </#if>
  /**
   * Gets the ${propertyName}.
   *
  <#if !propertyDescriptor.computed>
    <#if isReferenceEntity>
      <#if reverseOneToOne>
   * @hibernate.one-to-one
        <#if composition>
   *           cascade = "persist,merge,save-update,delete"
        </#if>
   *           property-ref = "${propertyDescriptor.reverseRelationEnd.name}"
        <#if !propertyDescriptor.versionControl>
   *           optimistic-lock = "false"
        </#if>
        <#if propertyDescriptor.fetchType?exists>
          <#if propertyDescriptor.fetchType.toString() = "JOIN">
   *           fetch = "join"
          <#elseif propertyDescriptor.fetchType.toString() = "SUBSELECT">
   *           fetch = "subselect"
          </#if>
        </#if>
        <#if propertyDescriptor.batchSize?exists>
   *           batch-size = "${propertyDescriptor.batchSize?c}"
        </#if>
      <#else>
   * @hibernate.many-to-one
        <#if !propertyDescriptor.versionControl>
   *           optimistic-lock = "false"
        </#if>
        <#if oneToOne>
          <#if composition>
   *           cascade = "persist,merge,save-update,delete"
          </#if>
   *           unique = "true"
        <#elseif bidirectional>
          <#if !managesPersistence>
   *           insert = "false"
   *           update = "false"
   *           not-null = "true"
          </#if>
        <#else>
          <#if composition>
   *           cascade = "persist,merge,save-update,delete"
          <#else>
               <#-- Referential relationship -->
   *           cascade = "none"
          </#if>
        </#if>
        <#if propertyDescriptor.fetchType?exists>
          <#if propertyDescriptor.fetchType.toString() = "JOIN">
   *           fetch = "join"
          <#elseif propertyDescriptor.fetchType.toString() = "SUBSELECT">
   *           fetch = "subselect"
          </#if>
        </#if>
        <#if propertyDescriptor.batchSize?exists>
   *           batch-size = "${propertyDescriptor.batchSize?c}"
        </#if>
        <#if isEntity>
          <#if fkName?exists>
   *           foreign-key = "${fkName}"
          <#else>
   *           foreign-key = "${tableName}_${propSqlName}_FK"
          </#if>
        </#if>
   * @hibernate.column
        <#if propSqlNameGenerated>
   *           name = "${propSqlName}_ID"
        <#else>
   *           name = "${propSqlName}"
        </#if>
        <#if propertyDescriptor.mandatory>
   *           not-null = "true"
        </#if>
        <#if propertyDescriptor.unicityScope?exists>
   *           unique-key = "${generateSQLName(propertyDescriptor.unicityScope)}_UNQ"
        </#if>
      </#if>
    <#elseif !isPurelyAbstract>
   * @hibernate.component
   *           prefix = "${propSqlName}_"
      <#if !propertyDescriptor.versionControl>
   *           optimistic-lock = "false"
      </#if>
    <#else>
   * @hibernate.any
      <#if idTypeName?exists>
   *           id-type = "${idTypeName}"
      <#else>
   *           id-type = "string"
      </#if>
      <#if !propertyDescriptor.versionControl>
   *           optimistic-lock = "false"
      </#if>
   * @hibernate.any-column
   *           name = "${propSqlName}_NAME"
      <#if propertyDescriptor.unicityScope?exists>
   *           unique-key = "${generateSQLName(propertyDescriptor.unicityScope)}_UNQ"
      </#if>
   * @hibernate.any-column
      <#if propSqlNameGenerated>
   *           name = "${propSqlName}_ID"
      <#else>
   *           name = "${propSqlName}"
      </#if>
      <#if idTypeLength?exists>
   *           length = "${idTypeLength?c}"
      </#if>
      <#if propertyDescriptor.mandatory>
   *           not-null = "true"
      </#if>
      <#if propertyDescriptor.unicityScope?exists>
   *           unique-key = "${generateSQLName(propertyDescriptor.unicityScope)}_UNQ"
      </#if>
    </#if>
  </#if>
   * @return the ${propertyName}.
   */
  ${propertyType} get${propertyName?cap_first}();
  
</#macro>

<#macro generatePropertyNameConstant propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  /**
   * Constant value for ${propertyName}.
   */
  String ${generateSQLName(propertyName)} = "${propertyName}";

</#macro>

<#macro generateEnumerationConstants propertyDescriptor>
  <#local propertyName=propertyDescriptor.name/>
  <#list propertyDescriptor.enumerationValues as enumerationValue>
  /**
   * Constant enumeration value for ${propertyName} : ${enumerationValue}.
   */
  String ${generateSQLName(propertyName + "_" + enumerationValue)} = "${enumerationValue}";

  </#list>
</#macro>

<#macro generateCollectionPropertyAccessors componentDescriptor propertyDescriptor>
  <@generatePropertyNameConstant propertyDescriptor=propertyDescriptor/>
  <@generateCollectionGetter componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  <#if propertyDescriptor.modifiable>
    <@generateCollectionSetter componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
    <@generateCollectionAdder componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
    <@generateCollectionRemer componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  </#if>

</#macro>

<#macro generateReferencePropertyAccessors componentDescriptor propertyDescriptor>
  <@generatePropertyNameConstant propertyDescriptor=propertyDescriptor/>
  <@generateComponentRefGetter componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  <#if propertyDescriptor.modifiable>
    <@generateEntityRefSetter componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  </#if>

</#macro>

<#macro generateScalarPropertyAccessors componentDescriptor propertyDescriptor>
  <@generatePropertyNameConstant propertyDescriptor=propertyDescriptor/>
  <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor")>
    <@generateEnumerationConstants propertyDescriptor=propertyDescriptor/>
  </#if>
  <@generateScalarGetter componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  <#if propertyDescriptor.modifiable>
    <@generateScalarSetter componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  </#if>

</#macro>

<#macro generatePropertyAccessors componentDescriptor propertyDescriptor>
  <#if instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor")>
    <@generateCollectionPropertyAccessors componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  <#elseif instanceof(propertyDescriptor, "org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor")>
    <@generateReferencePropertyAccessors componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  <#else>
    <@generateScalarPropertyAccessors componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
  </#if>

</#macro>
<@generateClassHeader componentDescriptor=componentDescriptor/>
<#if componentDescriptor.declaredPropertyDescriptors?exists>
  <#assign empty=true/>
  <#list componentDescriptor.declaredPropertyDescriptors as propertyDescriptor>
    <#if propertyDescriptor.name != "id" && propertyDescriptor.name != "version">
      <@generatePropertyAccessors componentDescriptor=componentDescriptor propertyDescriptor=propertyDescriptor/>
      <#assign empty=false/>
    </#if>
  </#list>
  <#if empty>
  // THIS IS JUST A MARKER INTERFACE.
  </#if>
<#else>
  // THIS IS JUST A MARKER INTERFACE.
</#if>
}
