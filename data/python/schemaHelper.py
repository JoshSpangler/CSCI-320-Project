"""
Filename: schemaHelper.py
Version: python 2.7
Author: Tim Johnson
Desc: Container file for all the helper constants for the schemas. The schemas are the following:
        engine_opt(engine_id, name, num_liters, num_cylinders)
        wheel_opt(wheel_id, diameter, name, style, run_flat)
        color_opt(color)
        body_design_opt(design_name)
        upholstery_opt(style, material_cost)
        model(model_name, brand_name, series, year, drivetrain, transmission, base_cost, engine_id)
        opt_upgrade(upgrade_name, added_cost)
        model_wheel_opt(model_name, wheel_id)
        model_color_opt(model_name, color)
        model_design_opt(model_name, design_name)
        model_upholstery_opt(model_name, style)
        vehicle_upgrade(VIN, upgrade_name)
        vehicle(VIN, model_name, wheel_id, color, design_name, style, day, month, year, price, 
                        dealer_id, sale_id, manufacturer_id)
        dealer(dealer_id, name, street, city, state, ZIP)
        sale(sale_id, dealer_id, customer, day, month, year)
        customer(customer_id, name, street, city, state, ZIP)
        cust_phone(customer_id, phone_num)
        person(customer_id, gender, annual_income)
        company(customer_id, size)
        manufacturer(manufacturer_id, name, street, city, state, ZIP)
        supplier(supplier_id, name, street, city, state, ZIP)
        supplies(supplier_id, manufacturer_id)
"""

from collections import namedtuple

OUTPUT_PATH        = '../output/'

ENGINE_OPT          = 'engine_opt'
WHEEL_OPT           = 'wheel_opt'
COLOR_OPT           = 'color_opt'
BODY_DESIGN_OPT     = 'body_design_opt'
UPHOLSTERY_OPT      = 'upholstery_opt'
MODEL               = 'model'
OPT_UPGRADE         = 'opt_upgrade'
MODEL_WHEEL_OPT     = 'model_wheel_opt'
MODEL_COLOR_OPT     = 'model_color_opt'
MODEL_DESIGN_OPT        = 'model_design'
MODEL_UPHOLSTERY_OPT    = 'model_upholstery_opt'
VEHICLE_UPGRADE         = 'vehicle_upgrade'
VEHICLE             = 'vehicle'
DEALER          = 'dealer'
SALE            = 'sale'
CUSTOMER        = 'customer'
CUST_PHONE      = 'cust_phone'
PERSON          = 'person'
COMPANY         = 'company'
MANUFACTURER    = 'manufacturer'
SUPPLIER        = 'supplier'
SUPPLIES        = 'supplies'

EngineOpt       = namedtuple('EngineOpt', ('engine_id', 'name', 'num_liters', 'num_cylinders'))
WheelOpt        = namedtuple('WheelOpt', ('wheel_id', 'diameter', 'name', 'style', 'run_flat'))
ColorOpt        = namedtuple('ColorOpt', 'color')
BodyDesignOpt   = namedtuple('BodyDesignOpt', 'design_name')
UpholsteryOpt   = namedtuple('UpholsteryOpt', ('style', 'material_cost'))
Model           = namedtuple('Model', ('model_name', 'brand_name', 'series', 'year', 'drivetrain',
                                       'transmission', 'base_cost', 'engine_id'))
OptUpgrade      = namedtuple('OptUpgrade', ('upgrade_name', 'added_cost'))
ModelWheelOpt   = namedtuple('ModelWheelOpt', ('model_name', 'wheel_id'))
ModelColorOpt   = namedtuple('ModelColorOpt', ('model_name', 'color'))
ModelDesignOpt      = namedtuple('ModelDesignOpt', ('model_name', 'design_name'))
ModelUpholsteryOpt  = namedtuple('ModelUpholsteryOpt', ('model_name', 'style'))
VehicleUpgrade      = namedtuple('VehicleUpgrade', ('VIN', 'upgrade_name'))
Vehicle         = namedtuple('Vehicle', ('VIN', 'model_name', 'wheel_id', 'color', 'design_name',
                                         'style', 'day', 'month', 'year', 'price', 'dealer_id',
                                         'sale_id', 'manufacturer_id'))
Dealer      = namedtuple('Dealer', ('dealer_id', 'name', 'street', 'city', 'state', 'ZIP'))
Sale        = namedtuple('Sale', ('sale_id', 'dealer_id', 'customer', 'day', 'month', 'year'))
Customer    = namedtuple('Customer', ('customer_id', 'name', 'street', 'city', 'state', 'ZIP'))
CustPhone   = namedtuple('CustPhone', ('customer_id', 'phone_num'))
Person      = namedtuple('Person', ('customer_id', 'gender', 'annual_income'))
Company     = namedtuple('Company', ('customer_id', 'size'))
Manufacturer = namedtuple('Manufacturer', ('manufacturer_id', 'name', 'street', 'city', 'state', 'ZIP'))
Supplier    = namedtuple('Supplier', ('supplier_id', 'name', 'street', 'city', 'state', 'ZIP'))
Supplies    = namedtuple('Supplies', ('supplier_id', 'manufacturer_id'))

TABLE_TYPES = {ENGINE_OPT: EngineOpt, WHEEL_OPT: WheelOpt, COLOR_OPT: ColorOpt,
               BODY_DESIGN_OPT: BodyDesignOpt, UPHOLSTERY_OPT: UpholsteryOpt, MODEL: Model,
               OPT_UPGRADE: OptUpgrade, MODEL_WHEEL_OPT: ModelWheelOpt, MODEL_COLOR_OPT: ModelColorOpt,
               MODEL_DESIGN_OPT: ModelDesignOpt, MODEL_UPHOLSTERY_OPT: ModelUpholsteryOpt,
               VEHICLE_UPGRADE: VehicleUpgrade, VEHICLE: Vehicle, DEALER: Dealer, SALE: Sale,
               CUSTOMER: Customer, CUST_PHONE: CustPhone, PERSON: Person, COMPANY: Company,
               MANUFACTURER: Manufacturer, SUPPLIER: Supplier, SUPPLIES: Supplies}


# noinspection PyProtectedMember
def outputToFile(schemaList):
    for name, ttype in TABLE_TYPES.iteritems():
        if ttype._fields == schemaList[0]._fields:
            with open(OUTPUT_PATH + name + '.csv', 'w') as f:
                for schema in schemaList:
                    f.write(','.join([str(x) for x in schema]) + '\n')
            return
