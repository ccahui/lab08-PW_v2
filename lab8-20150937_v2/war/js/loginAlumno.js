$(document).ready(function () {
    /*Para enviar los datos como lo realizaremos mediante
     * AJAX */

    $('#enviar').click(function () {
        /*EL objeto this, es el formulario con el metodo serialize convierte cada uno de los 
         * en Un objeto JSON para enviar al servidor*/

        var datosJSON = $('#form1').serialize();
        var metodo = $('#form1').attr("method");
        var action = $('#form1').attr("action");

        $.ajax({
            beforeSend: function () {
                //Muestra simbolo de cargando ...
                $('#cargando').attr('class', 'preloader1');
                $('#resultado').text("");
            },
            method: metodo,
            url: action,
            data: datosJSON,
            success: function (data) {
                console.log(data);
                //Escribe la respuesta y oculta el simbolo de cargando ...
                $('#cargando').removeClass('preloader1');

                if (data === "1")
                    window.location.assign("inicioAlumno");

                else if (data === '0') {
                    var mensaje = "Cuenta o Password incorrecto";

                    $('#resultado').html("<div class='text-danger '> \n\
                           <strong>" + "" + mensaje +
                            "</strong></div>");
                } else {
                    $('#resultado').html("<div class='text-danger '> \n\
                           <strong>" + "" + data +
                            "</strong></div>");
                }
            },
            error: function (data) {
                //Si ocurre un error, por ejemplo la pagina dejo de exisitir
                //Oculta el simobolo de Cargando y muestra el mensaje
                $('#cargando').removeClass('preloader1');

                $('#resultado').text("Se produjo un ERROR");
                console.log("Error " + data);
            },
            /*Sirve el tiempo de espera maximo para la respuesta*/
            timeout: 10000

        });
        return false;
    });

    $('#reset').click(function () {
        $('#form1')[0].reset();

    });

});









      