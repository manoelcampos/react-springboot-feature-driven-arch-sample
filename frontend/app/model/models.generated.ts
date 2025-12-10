/* tslint:disable */
/* eslint-disable */

/**
 * [A City somewhere in the world.
 * See , {@link AbstractBaseModel},  for the reason why all atributes are public.]
 * author @author Manoel Campos
 */
export interface City extends AbstractBaseModel {
    name: string;
    /**
     * The country district where the city is.
     */
    district: District;
}

/**
 * [See , {@link AbstractBaseModel},  for the reason why all atributes are public.]
 * author @author Manoel Campos
 */
export interface Customer extends AbstractBaseModel {
    name: string;
    socialSecurityNumber: string;
    /**
     * The city where the customer lives.
     */
    city?: City | null;
}

/**
 * [See , {@link AbstractBaseModel},  for the reason why all atributes are public.]
 * author @author Manoel Campos
 */
export interface District extends AbstractBaseModel {
    name: string;
    abbreviation: string;
}

/**
 * [A product that can be sold in the store.
 * 
 * See , {@link AbstractBaseModel},  for the reason why all atributes are public.]
 * author @author Manoel Campos
 */
export interface Product extends AbstractBaseModel {
    description: string;
    price: number;
    amount: number;
}

/**
 * [A purchase from a given , {@link Customer}, .
 * See , {@link AbstractBaseModel},  for the reason why all atributes are public.
 * We cannot call the class Order, since it is a reserved word in SQL,
 * otherwise, we need to explicitly change the table name.]
 * author @author Manoel Campos
 */
export interface Purchase extends AbstractBaseModel {
    customer: Customer;
    /**
     * The field cannot be named "timestamp" since this is a reserved work for many databases.
     */
    dateTime?: Date | null;
    itens?: PurchaseItem[] | null;
}

/**
 * [See , {@link AbstractBaseModel},  for the reason why all atributes are public.]
 * author @author Manoel Campos
 */
export interface PurchaseItem extends AbstractBaseModel {
    /**
     * Product being sold.
     * The field value is ignored in updates.
     * If a change is made to any item, the value of this attribute is disregarded.
     * After the sale is entered, the product cannot be changed.
     */
    product: Product;
    /**
     * Quantity of product items sold.
     * The field value is ignored in updates.
     * If a change is made to any item, the value of this attribute is disregarded.
     * After the sale is entered, the quantity cannot be changed.
     */
    quant: number;
}

/**
 * [A base class for implementing JPA Entities.
 * All classes that have the , {@link Entity},  annotation must inherit from this class.
 * Those classes have all atributes define as public, since the
 * [auto-class-accessors-maven-plugin](https://github.com/manoelcampos/auto-class-accessors-maven-plugin) is being used.
 * This way, when there is a read/write to a field,
 * the respective getter/getter is called instead (if existing).
 * The plugin is just included inside the pom.xml and the m`agic happens when the project is built.]
 * author @author Manoel Campos
 */
export interface AbstractBaseModel extends BaseModel {
}

/**
 * A ,{@link DTORecord Data Transfer Object}, for ,{@link City},.
 * param @param district The country district where the city is.
 */
export interface CityDTO extends DTORecord<City> {
    id?: number | null;
    name: string;
    districtId: number;
}

/**
 * A ,{@link DTORecord Data Transfer Object}, for ,{@link Customer},.
 * param @param city The city where the customer lives.
 */
export interface CustomerDTO extends DTORecord<Customer> {
    id?: number | null;
    name: string;
    socialSecurityNumber: string;
    cityId?: number | null;
}

/**
 * A ,{@link DTORecord Data Transfer Object}, for ,{@link District},.
 */
export interface DistrictDTO extends DTORecord<District> {
    id?: number | null;
    name: string;
    abbreviation: string;
}

/**
 * A ,{@link DTORecord Data Transfer Object}, for ,{@link Product},.
 */
export interface ProductDTO extends DTORecord<Product> {
    id?: number | null;
    description: string;
    price: number;
    amount: number;
}

/**
 * A ,{@link DTORecord Data Transfer Object}, for ,{@link Purchase},.
 * param @param dateTime The field cannot be named "timestamp" since this is a reserved work for many databases.
 */
export interface PurchaseDTO extends DTORecord<Purchase> {
    id?: number | null;
    customerId: number;
    dateTime?: Date | null;
    itens?: PurchaseItemDTO[] | null;
}

/**
 * A ,{@link DTORecord Data Transfer Object}, for ,{@link PurchaseItem},.
 * param @param product Product being sold. The field value is ignored in updates. If a change is made to any item, the value of this attribute is disregarded. After the sale is entered, the product cannot be changed.
 * param @param quant Quantity of product items sold. The field value is ignored in updates. If a change is made to any item, the value of this attribute is disregarded. After the sale is entered, the quantity cannot be changed.
 */
export interface PurchaseItemDTO extends DTORecord<PurchaseItem> {
    id?: number | null;
    purchaseId: number;
    productId: number;
    quant: number;
}

/**
 * An interface to be implemented by classes that represent a JPA Entity.
 * author @author Manoel Campos
 */
export interface BaseModel extends Serializable {
    id?: number | null;
}

export interface Serializable {
}

export interface DTORecord<T> {
}
