function mostrarFilaNueva(botonInsertar) {

    var fila = $(botonInsertar).parent().parent().next();
    return fila;

}
//Obtengo las Notas de los <Td>
function arrayFilaNota(botonInsertar) {
    var n = 3;
    var array = new Array(n);
    var arrayTd = $(botonInsertar).parent().siblings();//Captura los nodos de tipo <td></td> los 5 existentes

    for (var i = 0; i < n; i++)
        array[i] = $(arrayTd[i + 2]).text();//Obtengo las Notas 
    return array;
}
//Obtengo los inputs ("FILA NUEVA")
function inputFilaNota(filaNueva) {
    var n = 3;
    var array = new Array(n);
    var arrayTd = $(filaNueva).children();//Captura los 5 td de los cuales 3 poseen Los inputs
    for (var i = 0; i < n; i++)
        array[i] = $(arrayTd[i + 2]).children()[0];//Captura los input

    return array;
}
//Obtengo los <td> desde actualizar 
function arrayActualizarNota(botonActualizar) {
    var n = 3;
    var array = new Array(n);

    var fila = $(botonActualizar).parent().parent().prev();

    var arrayTd = $(fila).children();
    for (var i = 0; i < n; i++)
        array [i] = arrayTd[i + 2]; //Obtengo las <td> par Actualizar las Notas 
    return array;

}

$(document).ready(function () {
     $("#myInput").on("keyup", function() {
               var value = $(this).val().toLowerCase();
               
           $("#myTable tr").filter(function() {
             if($(this).attr('class')===undefined) 
             $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });});
    
    $('.insertar').click(function () {

        var filaNueva = mostrarFilaNueva(this);
        var arrayNota = arrayFilaNota(this);//Cotiene la etiqueta <Td> con las Notas
        var arrayInput = inputFilaNota(filaNueva);

        for (var i = 0; i < arrayNota.length; i++) {
            arrayInput[i].value = arrayNota[i];
            $(arrayInput[i]).attr("disabled", true);
        }

        $(filaNueva).show();//Muestra la Nueva Fila Con datos
    });
    $('.cancelar').click(function () {
        var filaNueva = mostrarFilaNueva(this);

        $(filaNueva).hide();//Oculto la Fila Nueva

    });

    $('.valor').parent().dblclick(function () {
        var input = $(this).children()[0];
        $(input).attr('disabled', false);
    });

    $('.actualizar').click(function () {
        var filaNueva = $(this).parent().parent();
        var arrayInput = inputFilaNota(filaNueva); //Los inputs con las notas Actualizadas
        var arrayActualizar = arrayActualizarNota(this);//Actualizar estos <td> con las notas de  los Inputs


        var curCod = $('#curCod').text();
        var tr = $(this).parent().parent().prev().children();
        var alumCod = $(tr[0]).text(); //AlumnoCodigo

        $.ajax({
            method: 'get',
            url: 'tablaNota',
            data: {
                n1: arrayInput[0].value, n2: arrayInput[1].value, n3: arrayInput[2].value, curCod: curCod, alumCod: alumCod
            },
            //Son los datos del servidor 
            /*beforeSend(Function)*/
            success: function (data) {
                /*Los datos son correctoz y se a insetado en la BD*/
                if (data === "ok") {
                    console.log(data);
                    /*Como los datos ya estan en BD se procedesn a actualizar en LA TABLA con JS sin NECESIDAD DE
                     * cargar de nuevo todo lso datos de LA BD , actuliza la BD actualiza en la pantalla con JS*/
                    for (var i = 0; i < arrayActualizar.length; i++) {
                        $(arrayActualizar[i]).text(arrayInput[i].value);
                    }
                } else {
                    window.alert("Se produjo el error en LA actuazlicion de datos en BD");
                    console.log(data);
                }
                $(filaNueva).hide();
            },
            error: function (data) {
                /*Erroe 404 , hay un parametro par tiempo de espera se sobreapaso ese tiempo de espera etc
                 * no logro la conexion con el servidor entr otros*/
                console.log(data);
            }
        });

    });



});