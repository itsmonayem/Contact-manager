console.log("script file")

const toggleSidebar = () => {
    console.log("CLick hoise")
    if ($(".sidebar").is(":visible")) {
        // close side bar
        $(".sidebar").css("display","none");
        $(".bar").css("display","block");
        $(".content").css("margin-left","25px");
    } else {
        // open sidebar
        $(".sidebar").css("display","block");
        $(".bar").css("display","none");
        $(".content").css("margin-left","20%");
    }
}


const search = () => {
    let query = $("#search-input").val();
    console.log(query);

    if (query==='') {
        $(".search-result").hide();
    } else {
        console.log(query);

        //sending request to server
        let url = `http://localhost:8080/search/${query}`;
        fetch(url)
            .then((response) => {
            return response.json();
        })
            .then((data) => {
                console.log(data);
                let text = `<div class="list-group">`;
                data.forEach((contact) => {
                    text += `<a href="#" class="list-group-item list-group-item-action">${contact.name}</a>`
                })
                text += `</div>`;
                $(".search-result").html(text);
                $(".search-result").show();
            });
    }
}