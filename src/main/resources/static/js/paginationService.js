export class PaginationServiceDto {
    constructor(url) {
        this.url
            = url;
    }

    getPage = async (page, items) => {
        const response = await fetch(`${this.url}?page=${page}&items=${items}`);
        return response.json();
    };
}