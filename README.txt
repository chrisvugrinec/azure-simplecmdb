WITH SuperMarktA AS (
    SELECT
        *
    FROM
        [eventhub-input]
    WHERE
        customerId like '%SuperMarktA%'
)
SELECT * INTO [servicebus-output] FROM SuperMarktA

Or like this:
SELECT * INTO [servicebus-output] FROM [eventhub-input] WHERE customerId like '%SuperMarktA%'
SELECT * INTO [servicebus-errors] FROM [eventhub-input] WHERE customerId like '%error%'

OR

WITH unknown AS (
    SELECT
        *
    FROM
        [eventhub-input]
    WHERE
        customerId = ''
)
SELECT * INTO [servicebus-errors] FROM unknown
SELECT * INTO [servicebus-output] FROM [eventhub-input] where customerId like '%SuperMarktA%'
