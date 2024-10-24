export class Date {
    day: number = 0;
    month: number = 0;
    year: number = 0;

    constructor(dayOrDataString?: number | string, month?: number, year?: number) {
        if (typeof dayOrDataString === 'string') {
            this.parseDateString(dayOrDataString);
        } else {
            this.day = dayOrDataString || 0;
            this.month = month || 0;
            this.year = year || 0;
        }
    }

    private parseDateString(dataString: string): void {
        if (dataString.includes('T')) {
            const data = dataString.split('T')[0].split('-');
            this.year = parseInt(data[0], 10);
            this.month = parseInt(data[1], 10);
            // to correct the timezone we do +1
            this.day = parseInt(data[2], 10) + 1;
            return;
        }

        if (dataString.includes('-')) {
            const data = dataString.split('-');
            this.year = parseInt(data[0], 10);
            this.month = parseInt(data[1], 10);
            this.day = parseInt(data[2], 10);
            return;
        }

        const data = dataString.replace(',', '').split(' ');
        this.month = this.convertMonth(data[0]);
        this.day = parseInt(data[1], 10);
        this.year = parseInt(data[2], 10);
    }

    private convertMonth(month: string): number {
        switch (month) {
            case 'Jan': return 1;
            case 'Feb': return 2;
            case 'Mar': return 3;
            case 'Apr': return 4;
            case 'May': return 5;
            case 'Jun': return 6;
            case 'Jul': return 7;
            case 'Aug': return 8;
            case 'Sep': return 9;
            case 'Oct': return 10;
            case 'Nov': return 11;
            case 'Dec': return 12;
            default: return 0;
        }
    }

    toString(): string {
        const month = this.month < 10 ? `0${this.month}` : this.month.toString();
        const day = this.day < 10 ? `0${this.day}` : this.day.toString();
        return `${this.year}-${month}-${day}`;
    }
}