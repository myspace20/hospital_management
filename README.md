# Hospital Database Normalization and Schema Design




## Normalization Process



### Employee

#### Unnormalized
**Table: Employee**
| employee_id | first_name | last_name | address | phone_number |
|-------------|------------|-----------|---------|--------------|


#### 1NF
- **Table: Employee**
  | employee_id | first_name | surname | street_name | city | country | phone_number |
  |-------------|------------|---------|-------------|------|---------|--------------|


#### 2NF

- **Table: Employee**
  | employee_id | first_name | surname | phone_number | address_id |
  |-------------|------------|---------|--------------|------------|

- **Table: Address**
  | address_id | street_name | city | country |
  |------------|-------------|------|---------|


### Department

#### Unnormalized
**Table: Department**
| code | name | building | director |
|------|------|----------|----------|


#### 1NF
- **Table: Department**
  | code | name | building_name | floor | wing | director_id |
  |------|------|---------------|-------|------|-------------|

#### 2NF

- **Table: Department**
  | code | name | building_id | floor_number | director_id |
  |------|------|-------------|--------------|-------------|

- **Table: Building**
  | building_id | name | wing |
  |-------------|------|------|


### Ward

#### Unnormalized
**Table: Ward**
| ward_id | ward_number | number_of_beds | supervisor |
|---------|-------------|----------------|------------|

#### 1NF

- **Table: Ward**
  | ward_id | ward_number | number_of_beds | supervisor_id |
  |---------|-------------|----------------|---------------|



### Patient

#### Unnormalized
**Table: Patient**
| patient_id | name | surname | address | phone_number |
|------------|------|---------|---------|--------------|


#### 1NF
| patient_id | first_name | surname | city | street | country | phone_number |
  |------------|------------|---------|------|--------|---------|--------------|

#### 2NF

| patient_id | first_name | surname | phone_number | address_id |
  |------------|------------|---------|--------------|------------|

### Admission

#### Unnormalized
**Table: Admission**
| bed | room | diagnosis | ward | doctor | bed_number | admission_date | discharge_date | patient_id |
|-----|------|-----------|------|--------|------------|----------------|----------------|


#### 1NF

| admission_id | bed_id | room_id | diagnosis | ward_id | doctor_id | admission_date | discharge_date |
  |--------------|--------|---------|-----------|---------|-----------|----------------|----------------|


#### 2NF

- **Table: Admission**
  | admission_id | bed_id | ward_id | doctor_id | admission_date | discharge_date |
  |--------------|--------|---------|-----------|----------------|----------------|

- **Table: Diagnosis**
  | diagnosis_id | name | 
  |--------------|-----------|

### Room

#### Unnormalized
**Table: Room**
| room_id | ward_no | room_number |
|---------|---------|-------------|

#### 1NF

- **Table: Room**
  | room_id | ward_id | room_number |
  |---------|---------|-------------|

#### 2NF
- **Table: Room**
  | room_id | ward_id | room_number |
  |---------|---------|-------------|

### Bed

#### Unnormalized
**Table: Bed**
| bed_id | room_id | bed_number | is_occupied |
|--------|---------|------------|-------------|


#### 1NF

- **Table: Bed**
  | bed_id | room_id | bed_number | is_occupied |
  |--------|---------|------------|-------------|


#### 2NF

- **Table: Bed**
  | bed_id | room_id | bed_number | is_occupied |
  |--------|---------|------------|-------------|


### Transfer

#### Unnormalized
**Table: Transfer**
| transfer_id | from_ward | to_ward | from_room | to_room | bed_number | admission_id | transfer_date |
|-------------|-----------|---------|-----------|---------|------------|--------------|---------------|


#### 1NF

- **Table: Transfer**
  | transfer_id | from_ward_id | to_ward_id | from_room_id | to_room_id | bed_id | admission_id | transfer_date |
  |-------------|--------------|------------|--------------|------------|--------|--------------|---------------|


#### 2NF

- **Table: Transfer**
  | transfer_id | from_ward_id | to_ward_id | from_room_id | to_room_id | bed_id | admission_id | transfer_date |
  |-------------|--------------|------------|--------------|------------|--------|--------------|---------------|


### Nurse

**Table: Nurse**
| employee_id | rotation | salary | department_code |
|-------------|----------|--------|-----------------|



### Doctor

**Table: Doctor**
| employee_id | specialty |
|-------------|-----------|




## SQL Schema

Below are the `CREATE TABLE` statements for the 2NF schema, with constraints to enforce data integrity.

```sql
CREATE TABLE Address (
    address_id INT PRIMARY KEY,
    street_name VARCHAR(100),
    city VARCHAR(50),
    country VARCHAR(50)
);

CREATE TABLE Employee (
    employee_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES Address(address_id)
);

CREATE TABLE Building (
    building_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    wing VARCHAR(20)
);

CREATE TABLE Department (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    building_id INT,
    floor_number INT,
    director_id INT,
    FOREIGN KEY (building_id) REFERENCES Building(building_id),
    FOREIGN KEY (director_id) REFERENCES Employee(employee_id)
);

CREATE TABLE Ward (
    ward_id INT PRIMARY KEY,
    number_of_beds INT NOT NULL,
    supervisor_id INT,
    FOREIGN KEY (supervisor_id) REFERENCES Employee(employee_id)
);

CREATE TABLE Room (
    room_id INT PRIMARY KEY,
    ward_id INT,
    room_number VARCHAR(20) NOT NULL,
    FOREIGN KEY (ward_id) REFERENCES Ward(ward_id)
);

CREATE TABLE Bed (
    bed_id INT PRIMARY KEY,
    room_id INT,
    bed_number VARCHAR(20) NOT NULL,
    is_occupied BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (room_id) REFERENCES Room(room_id)
);

CREATE TABLE Patient (
    patient_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20),
    address_id INT,
    FOREIGN KEY (address_id) REFERENCES Address(address_id)
);

CREATE TABLE Diagnosis (
    diagnosis_id INT PRIMARY KEY,
    diagnosis VARCHAR(200) NOT NULL
);

CREATE TABLE Admission (
    admission_id INT PRIMARY KEY,
    bed_id INT,
    ward_id INT,
    doctor_id INT,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    FOREIGN KEY (bed_id) REFERENCES Bed(bed_id),
    FOREIGN KEY (ward_id) REFERENCES Ward(ward_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctor(employee_id)
);

CREATE TABLE AdmissionDiagnosis (
    admission_id INT,
    diagnosis_id INT,
    PRIMARY KEY (admission_id, diagnosis_id),
    FOREIGN KEY (admission_id) REFERENCES Admission(admission_id),
    FOREIGN KEY (diagnosis_id) REFERENCES Diagnosis(diagnosis_id)
);

CREATE TABLE Transfer (
    transfer_id INT PRIMARY KEY,
    from_ward_id INT,
    to_ward_id INT,
    from_room_id INT,
    to_room_id INT,
    bed_id INT,
    admission_id INT,
    transfer_date DATE NOT NULL,
    FOREIGN KEY (from_ward_id) REFERENCES Ward(ward_id),
    FOREIGN KEY (to_ward_id) REFERENCES Ward(ward_id),
    FOREIGN KEY (from_room_id) REFERENCES Room(room_id),
    FOREIGN KEY (to_room_id) REFERENCES Room(room_id),
    FOREIGN KEY (bed_id) REFERENCES Bed(bed_id),
    FOREIGN KEY (admission_id) REFERENCES Admission(admission_id)
);

CREATE TABLE Nurse (
    employee_id INT PRIMARY KEY,
    rotation VARCHAR(50),
    salary DECIMAL(10, 2),
    department_code VARCHAR(10),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
    FOREIGN KEY (department_code) REFERENCES Department(code)
);

CREATE TABLE Doctor (
    employee_id INT PRIMARY KEY,
    specialty VARCHAR(100),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);
```

## Data Models

### Conceptual Model
The conceptual model captures high-level entities and relationships without implementation details.

#### Entities and Relationships
- **Entities**: Employee, Department, Ward, Patient, Admission, Room, Bed, Transfer, Nurse, Doctor, Building, Address, Diagnosis.
- **Relationships**:
  - Employee (Nurse, Doctor) works in Department.
  - Department is located in Building.
  - Ward is supervised by Employee.
  - Room belongs to Ward.
  - Bed belongs to Room.
  - Patient is admitted via Admission.
  - Admission involves Bed, Ward, Doctor, and Diagnosis.
  - Transfer moves Admission between Wards/Rooms/Beds.



### Logical Model
The sql schema above is used for the logical data model.





