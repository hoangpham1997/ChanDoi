export class TreeviewItem {
    text: string;
    value: any;
    select: boolean;
    check: boolean;
    parentCompany: boolean;
    accType: number;
    isHidden: boolean;
    children: TreeviewItem[];
    workingOnBook: string;
}
