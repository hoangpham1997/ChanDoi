const numberOfEmptyRow = 10;

export function getEmptyRow(list, num = numberOfEmptyRow) {
    const emptyRow = [];
    const length = list ? list.length : 0;
    if (length < num) {
        for (let i = 0; i < num - length; i++) {
            emptyRow.push({});
        }
    }
    return emptyRow;
}
