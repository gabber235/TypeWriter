DEFINE TABLE OVERWRITE mutation_receipt SCHEMAFULL PERMISSIONS NONE;
DEFINE FIELD OVERWRITE request ON mutation_receipt TYPE array<int>;
DEFINE FIELD OVERWRITE result ON mutation_receipt TYPE object FLEXIBLE;
DEFINE FIELD OVERWRITE committed_at ON mutation_receipt TYPE datetime DEFAULT time::now() READONLY;

DEFINE FUNCTION OVERWRITE fn::mutation::recall($receipt: record<mutation_receipt>, $request: array<int>) {
    LET $stored = SELECT * FROM ONLY $receipt;
    IF $stored = NONE { RETURN NONE };
    IF $stored.request != $request { THROW 'operation-identity-reused-error' };
    RETURN $stored.result;
} PERMISSIONS NONE;

DEFINE FUNCTION OVERWRITE fn::mutation::commit($receipt: record<mutation_receipt>, $request: array<int>, $result: object) {
    LET $stored = CREATE ONLY $receipt SET request = $request, result = $result;
    RETURN $stored.result;
} PERMISSIONS NONE;
